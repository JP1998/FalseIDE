/*
 *      Copyright 2016 Jean-Pierre Hotz
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * TODO:
 *
 * - add decompilation functionality
 *      - provide path to byte-file
 *      - shown as:
 *          - code in dialog
 *          - temporary project
 *              - in RAM -> set currentProject to what?
 *                  -> would require complete rework
 *              - saved as project "temp"
 *                  - exclude project name "temp"
 *
 * TODO:
 */

package de.jeanpierrehotz.falseide;

import com.sun.istack.internal.Nullable;
import de.jeanpierrehotz.falseide.ressources.Ressources;
import de.jeanpierrehotz.falseide.ui.DecompileDialog;
import de.jeanpierrehotz.falseide.ui.maindialogs.*;
import de.jeanpierrehotz.falseide.ui.FileTreeNode;
import de.jeanpierrehotz.falseide.ui.JMenuCompat;
import de.jeanpierrehotz.falseide.ui.JMenuItemCompat;
import de.jeanpierrehotz.falseide.ui.ideui.FalseIDEGraphicUserInterface;
import de.jeanpierrehotz.falseide.ui.settingsui.ChangeSettingsDialog;
import de.jeanpierrehotz.falseide.utils.ErrorLogger;
import de.jeanpierrehotz.falseide.utils.FalseIDESettings;
import de.jeanpierrehotz.falseide.utils.Preference;
import de.jeanpierrehotz.falseprogramminglanguage.compiler.FalseCompiler;
import de.jeanpierrehotz.falseprogramminglanguage.compiler.error.CompileErrorCollection;
import de.jeanpierrehotz.falseprogramminglanguage.data.FalseProgram;
import de.jeanpierrehotz.falseprogramminglanguage.data.error.InterpretError;
import de.jeanpierrehotz.falseprogramminglanguage.interpreter.FalseInterpreter;
import de.jeanpierrehotz.falseprogramminglanguage.language.FalseLanguageDefinition;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class provides the main frame and the functionality for the UI of the FalseIDE
 */
public class FalseIDE extends JFrame {

    /**
     * This {@link ErrorLogger} object is the logger that is in charge of the logging of any error happening in this program
     */
    private ErrorLogger log;
    /**
     * The Preference we need for this program. If it is not existent we'll create the default one.
     * This Preference is read from following path:
     * <pre><code>&lt;path this program resides in&gt;&lt;{@link Ressources#APPLICATION_PREFERENCE_NAME}&gt;</code></pre>
     * The path this program resides in is retrieved by the code
     * <pre><code><span style="color: blue;">new</span> File(<span style="color: #007700;">""</span>).getAbsolutePath()</code></pre>
     * Thus if this program is executed from a .jar-File with the path <pre><code>C:\Users\Admin\Desktop\FalseIDE.jar</code></pre>
     * this code will return <pre><code>C:\Users\Admin\Desktop\</code></pre>
     */
    private Preference applicationPreference;
    /**
     * The settings for this instance of the FalseIDE. They may be altered by the user through the {@link ChangeSettingsDialog}.
     * The settings are loaded from the applications preferences.
     */
    private FalseIDESettings settings;
    /**
     * The name of the currently opened project
     */
    private String currentProject;
    /**
     * Whether the source code of the currently opened project has been changed
     */
    private boolean currentProjectSaved;
    /**
     * The actual UI we want to show in this JFrame
     */
    private FalseIDEGraphicUserInterface uiForm;
    /**
     * The PopupMenu used for the project tree if there is no project selected.
     * It gives possibility to:
     * <ul>
     * <li>create a new project</li>
     * <li>refresh the workspace</li>
     * </ul>
     */
    private PopupMenu fileTreePopupMenu;
    /**
     * The PopupMenu used for the project tree if there is a project selected.
     * It gives possibility to:
     * <ul>
     * <li>open the selected project</li>
     * <li>compile the selected project</li>
     * <li>execute the selected project</li>
     * <li>delete the selected project</li>
     * </ul>
     * I want to also add the possibility to decompile False code from .out-Files
     */
    private PopupMenu fileTreeItemSelectedPopupMenu;
    /**
     * The leaf of the projectTree that has lastly been clicked.
     * May be null, if the user clicked into the {@link FalseIDEGraphicUserInterface#getFileTree()} at a point where
     * no leaf is shown or if in the menu bar an item like "compile project" has been clicked.
     */
    private TreePath lastClickedItem;
    /**
     * The interpreter of the program that has been executed lastly.
     * If there has not been any program being executed yet this field will be null.
     */
    private FalseInterpreter currentProgramsInterpreter;
    /**
     * Whether there is a program that is currently being executed.
     * Restricts actions like compiling or executing a project and exiting the application.
     */
    private boolean currentlyExecuted;

    /**
     * The constructor for a FalseIDE.
     * This will open up the main frame of the generated instance
     */
    public FalseIDE() {
        // first we'll initialize th UI
        initializeLayout();
        // and after that we'll load up any data we need and (if needed) show this data to the user
        onWindowCreated();
    }

    /**
     * This method initializes the layout as far as possible without needing any data
     */
    private void initializeLayout() {
//      set the title
        setTitle(Ressources.APPLICATION_NAME);

//      creating the actual UI used in this JFrame
        this.uiForm = new FalseIDEGraphicUserInterface();

//      create the menu bar
        JMenuBar bar = new JMenuBar();

        bar.add(new JMenuCompat(
                Ressources.MENUBAR_FILE_CAPTION,
                new JMenuItemCompat(Ressources.MENUBAR_FILE_CREATEPROJECT_CAPTION, KeyEvent.VK_N, ev -> createProject()),
                new JMenuItemCompat(Ressources.MENUBAR_FILE_SAVEPROJECT_CAPTION, KeyEvent.VK_S, ev -> saveProject()),
                new JMenuItemCompat(Ressources.MENUBAR_FILE_SETTINGS_CAPTION, KeyEvent.VK_W, ev -> editSettings()),
                JMenuCompat.SEPARATORITEM,
                new JMenuItemCompat(Ressources.MENUBAR_FILE_EXITAPPLICATION_CAPTION, ev -> FalseIDE.this.dispose())
        ));

        bar.add(new JMenuCompat(
                Ressources.MENUBAR_CODE_CAPTION,
                new JMenuItemCompat(Ressources.MENUBAR_CODE_COMPILE_CAPTION, KeyEvent.VK_P, new ImageIcon(FalseIDE.class.getResource(Ressources.MENUBAR_CODE_ICON_COMPILE)), ev -> {
                    lastClickedItem = null;
                    compileProject();
                }),
                new JMenuItemCompat(Ressources.MENUBAR_CODE_EXECUTE_CAPTION, KeyEvent.VK_E, new ImageIcon(FalseIDE.class.getResource(Ressources.MENUBAR_CODE_ICON_EXECUTE)), ev -> {
                    lastClickedItem = null;
                    executeProject();
                }),
                new JMenuItemCompat(Ressources.MENUBAR_CODE_DECOMPILE_CAPTION, new ImageIcon(FalseIDE.class.getResource(Ressources.MENUBAR_CODE_ICON_DECOMPILE)), ev -> decompileProject()),
                new JMenuItemCompat(Ressources.MENUBAR_CODE_EXECUTEWITHOUTCOMPILING_CAPTION, new ImageIcon(FalseIDE.class.getResource(Ressources.MENUBAR_CODE_ICON_EXECUTEWITHOUTCOMPILATION)), ev -> {
                    lastClickedItem = null;
                    executeProjectWithoutCompiling();
                })
        ));

        bar.add(new JMenuCompat(
                Ressources.MENUBAR_ERRORLOG_CAPTION,
                new JMenuItemCompat(Ressources.MENUBAR_ERRORLOG_SHOW_CAPTION, ev -> showLog()),
                new JMenuItemCompat(Ressources.MENUBAR_ERRORLOG_SAVE_CAPTION, ev -> saveLog()),
                new JMenuItemCompat(Ressources.MENUBAR_ERRORLOG_CLEAR_CAPTION, ev -> clearLog())
        ));

        setJMenuBar(bar);

//      create the pop-up menu for the project tree
        this.fileTreePopupMenu = new PopupMenu();
        MenuItem createFileMenuItem = new MenuItem(Ressources.CONTEXTMENU_NOPROJECTSELECTED_CREATENEWPROJECT);
        createFileMenuItem.addActionListener((evt) -> createProject());
        this.fileTreePopupMenu.add(createFileMenuItem);

        this.fileTreePopupMenu.addSeparator();

        MenuItem refreshWorkspaceMenuItem = new MenuItem(Ressources.CONTEXTMENU_NOPROJECTSELECTED_REFRESHWORKSPACE);
        refreshWorkspaceMenuItem.addActionListener((evt) -> refreshFileTree());
        this.fileTreePopupMenu.add(refreshWorkspaceMenuItem);

        this.uiForm.getFileTree().add(this.fileTreePopupMenu);

        this.fileTreeItemSelectedPopupMenu = new PopupMenu();

        MenuItem openMenuItem = new MenuItem(Ressources.CONTEXTMENU_PROJECTSELECTED_OPENPROJECT);
        openMenuItem.addActionListener((evt) -> openProject());
        this.fileTreeItemSelectedPopupMenu.add(openMenuItem);

        MenuItem compileMenuItem = new MenuItem(Ressources.CONTEXTMENU_PROJECTSELECTED_COMPILEPROJECT);
        compileMenuItem.addActionListener((evt) -> compileProject());
        this.fileTreeItemSelectedPopupMenu.add(compileMenuItem);

        MenuItem executeMenuItem = new MenuItem(Ressources.CONTEXTMENU_PROJECTSELECTED_EXECUTEPROJECT);
        executeMenuItem.addActionListener((evt) -> executeProject());
        this.fileTreeItemSelectedPopupMenu.add(executeMenuItem);

        MenuItem executeWithoutCompilingMenuItem = new MenuItem(Ressources.CONTEXTMENU_PROJECTSELECTED_EXECUTEPROJECTWITHOUTCOMPILING);
        executeWithoutCompilingMenuItem.addActionListener((evt) -> executeProjectWithoutCompiling());
        this.fileTreeItemSelectedPopupMenu.add(executeWithoutCompilingMenuItem);

        this.fileTreeItemSelectedPopupMenu.addSeparator();

        MenuItem deleteMenuItem = new MenuItem(Ressources.CONTEXTMENU_PROJECTSELECTED_DELETEPROJECT);
        deleteMenuItem.addActionListener((evt) -> deleteProject());
        this.fileTreeItemSelectedPopupMenu.add(deleteMenuItem);

        this.uiForm.getFileTree().add(this.fileTreeItemSelectedPopupMenu);

//      add the mouselistener to the projecttree to detect all the clicks
//      onto projects or somewhere else
        this.uiForm.getFileTree().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTree src = (JTree) evt.getSource();

//              we receive the project at the position that was clicked
                lastClickedItem = src.getPathForLocation(evt.getX(), evt.getY());

//              if the left button has been clicked
                if (SwingUtilities.isLeftMouseButton(evt)) {
//                  if it's the second time clicking and the user clicked onto a project
                    if (evt.getClickCount() == 2 && lastClickedItem != null) {
//                      we'll open the specified project
                        openProject();
                    }
                }
//              otherwise if the right button has been clicked
                else if (SwingUtilities.isRightMouseButton(evt)) {
//                  and the user didn't click onto a project
                    if (lastClickedItem == null) {
//                      we'll show the pop-up menu that gives possibilities to create a new project or refresh the workspace
                        fileTreePopupMenu.show(src, evt.getX(), evt.getY());
                    }
//                  otherwise, if the user did click onto a project
                    else {
//                      we'll show the pop-up menu that gives possibility to open, compile, execute or delete the given project
                        fileTreeItemSelectedPopupMenu.show(src, evt.getX(), evt.getY());
                    }
                }
            }
        });

//      add a actionlistener to the abortionbutton
        this.uiForm.getAbortExecutionButton().addActionListener(ev -> abortExecution());

//      make the ui displayable
        getContentPane().add(this.uiForm.getContentPanel());
        setLocationRelativeTo(null);
        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocation(20, 20);

//      add a window listener to the ui
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                onWindowClosed();
            }
        });

//      and show the frame
        setVisible(true);
    }

    /**
     * This method loads all the data needed and updates the UI accordingly to the data
     */
    private void onWindowCreated() {
//      load up the preferences we need for the application
        applicationPreference = new Preference(new File("").getAbsolutePath(), Ressources.APPLICATION_PREFERENCE_NAME);

//      load all the settings from the preferences
        settings = new FalseIDESettings(applicationPreference);

//      load the current project from the preferences
        currentProject = applicationPreference.getString(Ressources.PreferenceKeys.PREFERENCE_LASTPROJECT_KEY, Ressources.DEFAULT_LASTPROJECT);

//      signalize that the current project is saved, that we have no interpreter at the moment and that there is no prgram being executed currently
        currentProjectSaved = true;

        currentProgramsInterpreter = null;
        currentlyExecuted = false;

//      we assign the KeyListener that detects any changes in the source code
        uiForm.addFileEditorKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
//              if the typed character is to be processed
                if (!e.isControlDown() && !e.isAltDown() && !e.isAltGraphDown() && !currentProject.equals("")) {
//                  we'll signalize that the project has not been saved since the last modification
                    currentProjectSaved = false;
                }

//              if the typd character begins a block in the code
                if (e.getKeyChar() == '[' || e.getKeyChar() == '{' || e.getKeyChar() == '"') {
//                  we'll end this block as a convenience for the user :)
                    char toAdd = ' ';

                    switch (e.getKeyChar()) {
                        case FalseLanguageDefinition.PRINT_STRING_OPERATION:
                            toAdd = FalseLanguageDefinition.PRINT_STRING_OPERATION;
                            break;
                        case FalseLanguageDefinition.LAMBDA_BEGIN:
                        case FalseLanguageDefinition.COMMENT_BEGIN:
                            toAdd = (char) (e.getKeyChar() + 2);
                            break;
                    }

                    int caretpos = uiForm.getFileEditorCaretPosition();
                    uiForm.setFileEditorText(uiForm.getFileEditorText().substring(0, caretpos) + toAdd + uiForm.getFileEditorText().substring(caretpos));
                    uiForm.setFileEditorCaretPosition(caretpos);
                }
            }
        });

//      restore the font sizes
        uiForm.setFileEditorFontSize(applicationPreference.getInt(Ressources.PreferenceKeys.PREFERENCE_FILEEDITOR_FONTSIZE_KEY, Ressources.DEFAULT_FILEEDITOR_FONTSIZE));
        uiForm.setConsoleFontSize(applicationPreference.getInt(Ressources.PreferenceKeys.PREFERENCE_CONSOLEEDITOR_FONTSIZE_KEY, Ressources.DEFAULT_CONSOLEEDITOR_FONTSIZE));

//      we instantiate the ErrorLogger with the log in the current application folder
        log = new ErrorLogger(new File("").getAbsolutePath());

//      open the project that has been opened the last time this application has been executed
        openProject(currentProject);

//      afterwards we refresh the file tree
        refreshFileTree();
    }

    /**
     * This method tries to close the Application and checks all the things we need to
     * before really closing it.
     */
    private void onWindowClosed() {
//      we won't allow the user to end the application if therre is a program currently running
        if (currentlyExecuted) {
            new ProgramAlreadyRunningDialog(this).setVisible(true);
            return;
        }

//      we'll show the user that his progress is not saved if... well if it has not been saved
        if (!currentProjectSaved) {
            UnsavedProgressDialog unsavedProgress = new UnsavedProgressDialog(this);
            unsavedProgress.setVisible(true);

//          also we'll abort closing the application if the user doesn't want to discard his progress
            if (!unsavedProgress.isToBeChanged()) {
                return;
            }
        }

//      close the log so that it is being refreshed
        log.writeLog();

//      afterwards we save the data we'll need the next time we launch the application
        settings.saveToPreferenceEditor(
                applicationPreference.edit()
                        .putString(Ressources.PreferenceKeys.PREFERENCE_LASTPROJECT_KEY, currentProject)
                        .putInt(Ressources.PreferenceKeys.PREFERENCE_FILEEDITOR_FONTSIZE_KEY, uiForm.getFileEditorFontSize())
                        .putInt(Ressources.PreferenceKeys.PREFERENCE_CONSOLEEDITOR_FONTSIZE_KEY, uiForm.getConsoleFontSize())
        ).commit();

//      last but not least we'll close the application
        dispose();
    }

    /**
     * This method refreshes the list of projects.
     */
    private void refreshFileTree() {
        FileTreeNode root = new FileTreeNode("");

//      we'll list all the files in the current workspace
        String[] temp = new File(settings.getWorkSpace()).list();

        ArrayList<String> files;

//      and filter those out that really represent a project
        if (temp != null) {
//          if there are files within the current workspace
            files = new ArrayList<>(Arrays.asList(temp)).stream()
                    .filter(path -> {
                        path = new File(settings.getWorkSpace() + path).getAbsolutePath();

                        if (path.endsWith(File.separator)) {
                            path = path.substring(0, path.length() - 1);
                        }
                        String projectName = path.substring(path.lastIndexOf(File.separator) + 1);

//                      we'll select those that are directories and contain a source code file in them
                        return new File(path).isDirectory() && new File(path + File.separator + projectName + Ressources.SOURCE_FILE_ENDING).exists();
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
//          if there are no files within the current workspace we need to initialize an empty array list
            files = new ArrayList<>();
        }

//      then we'll populate the root node with all the projects represented as individual nodes
        for (int i = 0; i < files.size(); i++) {
            root.insert(new FileTreeNode(files.get(i)), i);
        }

//      create a tree model of the root node and all its branches
        DefaultTreeModel path = new DefaultTreeModel(root);

//      and lastly show the tree model with all the projects
        this.uiForm.getFileTree().setModel(path);
    }

    /**
     * This method tries to save the currently opened project.<br>
     * It will show a {@link IOErrorDialog} if the saving fails and set {@link #currentProjectSaved} to {@code true}
     * if the saving succeeds
     *
     * @return {@code true} if the saving successes; {@code false} if the saving fails
     */
    private boolean saveProject() {
        try {
//          write the content of the editor to the source file
            BufferedWriter write = new BufferedWriter(new FileWriter(new File(settings.getWorkSpace() + currentProject + File.separator + currentProject + Ressources.SOURCE_FILE_ENDING)));
            write.write(uiForm.getFileEditorText());
            write.close();

//          indicate that the project has been saved
            currentProjectSaved = true;
//          and indicate the success
            return true;
        } catch (IOException e) {
//          if the user wants moderate errors to be logged
            if (settings.isToBeLogged(Ressources.LoggingLevels.MODERATE)) {
//              log the error
                log.logMessage(Ressources.ERROR_SAVEPROJECT_IOEXCEPTION);
                log.logThrowable(e);
            }

//          show that there was an io error
            new IOErrorDialog(this).setVisible(true);
//          and indicate the error
            return false;
        }
    }

    /**
     * This method checks whether the current project has been saved, and open the
     * selected project if the user wants us to do so.
     */
    private void openProject() {
        if (!currentProjectSaved) {
//          show that there is unsaved progress
            UnsavedProgressDialog unsavedProgress = new UnsavedProgressDialog(this);
            unsavedProgress.setVisible(true);

//          if the user does not want to close the project regardless we'll simply return
            if (!unsavedProgress.isToBeChanged()) {
                return;
            }
        }

//      if the project had been saved anyways or if the user wants to close the current project
//      anyways we'll open the currently selected project.
//      The selected project should therefor not be null, which is made sure by the fact that this method
//      is only called in places where this is guaranteed
        openProject(lastClickedItem.getLastPathComponent().toString());
    }

    /**
     * This mehtod opens the project with the given name.
     * Calls will be ignored, if the project name is empty or any {@link IOException} occurs
     * while loading from the source code file.
     *
     * @param projectname The name of the project that is to be loaded.
     */
    private void openProject(String projectname) {
//      ignore calls with empty project names
        if (projectname.trim().equals("")) {
            return;
        }

//      ignore calls if there is a IOException caught
        String content = getFileContent(projectname);
        if (content == null) {
            return;
        }

//      otherwise we'll show the source code and open the given project
        uiForm.setFileEditorText(content);
        currentProject = projectname;
        currentProjectSaved = true;
    }

    /**
     * This mehtod prompts the user to input the name of a new project and checks whether the current
     * project has already been saved.
     */
    private void createProject() {
//      prompt the user to put in the name of the project
        CreateProjectDialog createProject = new CreateProjectDialog(FalseIDE.this);
        createProject.setVisible(true);

//      if the user wants the project to be created
        if (createProject.isToBeCreated()) {
//          we check whether the current project has already been saved
            if (!currentProjectSaved) {
//              if not we'll show the user that the current project has not been saved
                UnsavedProgressDialog unsavedProgress = new UnsavedProgressDialog(this);
                unsavedProgress.setVisible(true);

//              if the user doesn't want to close the current project anyways
                if (!unsavedProgress.isToBeChanged()) {
//                  we'll return
                    return;
                }
            }

//          if the user wants to create the project and it has been saved or the user wants to
//          close the current project anyways we'll create the project with the given name
            createProject(createProject.getProjectName());
        }
    }

    /**
     * This method tries to create a project with the given name and (if successful) opens it
     *
     * @param projectname the name of the project that should be created
     */
    private void createProject(String projectname) {
        try {
//          if the creation of the project directory and the source code file is successful
            if (new File(settings.getWorkSpace() + projectname).mkdirs()
                    && new File(settings.getWorkSpace() + projectname + File.separator + projectname + Ressources.SOURCE_FILE_ENDING).createNewFile()) {
//              we'll refresh the file tree (showing the projects to the user)
                refreshFileTree();
//              and open the new project
                openProject(projectname);
            }
//          otherwise if one of the two things fail (which is very unlikely) we'll show an IOErrorDialog to the user
            else {
                new IOErrorDialog(this).setVisible(true);
            }
        } catch (IOException e) {
//          if the user wants moderate errors to be logged
            if (settings.isToBeLogged(Ressources.LoggingLevels.MODERATE)) {
//              log the error
                log.logMessage(Ressources.ERROR_CREATEPROJECT_IOEXCEPTION);
                log.logThrowable(e);
            }

//          if there was a IOException we'll also show a IOErrorDialog to the user
            new IOErrorDialog(this).setVisible(true);
        }
    }

    /**
     * This method is used to close the current project without loading up another one
     */
    private void closeProject() {
        uiForm.setFileEditorText("");
        currentProject = "";
    }

    /**
     * This method tries to compile a project and handles any errors that could occur in the process.<br>
     * The return value of this method resembles whether the compilation process was successful or not
     *
     * @return The result of the compilation process
     */
    @Nullable
    private CompilationResult compileProject() {
//      we'll clear the console to make the output more clearly for the user
        this.uiForm.clearConsole();

//      we'll restrict this action if there is a program already running
        if (!currentlyExecuted) {
            String projectToCompile;

//          if the project that is to be compiled has been clicked from the project tree
            if (lastClickedItem != null) {
//              we'll get the name of project that has been clicked
                projectToCompile = lastClickedItem.getLastPathComponent().toString();
            }
//          otherwise, we'll compile the current project
            else {
//              therefor we'll save it
                if (!saveProject()) {
//                  and if there was a error while saving we'll stop compilation
                    return new CompilationResult(null, false);
                }
                projectToCompile = currentProject;
            }

            try {
//              then we'll compile the source code
                FalseProgram prog = FalseCompiler.compile(getFileContent(projectToCompile));

//              and save its byte code to the byte file
                prog.saveByteCode(settings.getWorkSpace() + projectToCompile + File.separator + projectToCompile + Ressources.BYTE_FILE_ENDING);

//              if there was no error or interruption whatsoever we'll print the success to the console
                System.out.println(Ressources.COMPILATION_ENDING_SUCCESS);
//              and of course return the succeeded compilation result
                return new CompilationResult(prog, true);
            }
//          if there was an error during compilation
            catch (CompileErrorCollection compileErrorCollection) {
//              if the user wants every error to be logged
                if (settings.isToBeLogged(Ressources.LoggingLevels.EVERY)) {
//                  log the error
                    log.logMessage(Ressources.ERROR_COMPILEPROJECT_COMPILEERROR);
                    log.logThrowable(compileErrorCollection);
                }

//              we'll print the errors found by the compiler to the console
                System.out.println(compileErrorCollection.getMessage());
//              and print the failure to the console
                System.out.println(Ressources.COMPILATION_ENDING_FAILED);
            }
//          if there was an error during file io
            catch (IOException e) {
//              if the user wants moderate errors to be logged
                if (settings.isToBeLogged(Ressources.LoggingLevels.MODERATE)) {
//                  log the error
                    log.logMessage(Ressources.ERROR_COMPILEPROJECT_IOEXCEPTION);
                    log.logThrowable(e);
                }

//              we'll show a IOErrorDialog to the user
                new IOErrorDialog(this).setVisible(true);
            }

        }
//      if there is already a program running we'll tell the user that he cannot do this while a
//      program is running
        else {
            new ProgramAlreadyRunningDialog(this).setVisible(true);
        }

//      if this method has not yet returned any value this means there has to have been an error
//      which means that we'll simply return false
        return new CompilationResult(null, false);
    }

    /**
     * This method gives the user to decompile byte code files by opening a {@link DecompileDialog}
     */
    private void decompileProject() {
//      open a resizable dialog that lets the user select a .out file and shows the decompiled file in a JEditorPane
        new DecompileDialog(this, log, settings).setVisible(true);
    }

    /**
     * This method tries to execute a project.
     * If specified project isn't already open we try to open it.
     * Afterwards it is compiled, and (if everything succeeds) finally actually executed
     */
    private void executeProject() {
//      restrict to one program running at a time
        if (!currentlyExecuted) {
//          if we're executing a project from the projects list
            if (lastClickedItem != null) {
//              and the current project is not saved
                if (!currentProjectSaved) {
//                  we'll show an dialog to the user showing him that his progress might be lost if he continued
                    UnsavedProgressDialog unsavedProgress = new UnsavedProgressDialog(this);
                    unsavedProgress.setVisible(true);

//                  if the user doesn't want to proceed we'll end this method
                    if (!unsavedProgress.isToBeChanged()) {
                        return;
                    }
                }

//              if the current project is saved or the user wants to proceed without saving
//              we'll open the project that was clicked
                openProject(lastClickedItem.getLastPathComponent().toString());
            }

//          we'll signalize that we want to compile the currently opened project
            lastClickedItem = null;

//          and finally compile the project
            CompilationResult res = compileProject();

//          if the compilation was not successful we'll stop the process
            if (!res.isSuccess()) {
                return;
            }

//          if we may proceed we'll create a interpreter for this program
            currentProgramsInterpreter = new FalseInterpreter(res.getResultedProgram());

//          we signalize that we do not want neither stack nor variable contents printed to stdout
            currentProgramsInterpreter.setPrintStack(false);
            currentProgramsInterpreter.setPrintVariables(false);

//          we give the interpreter an executionlistener, that simply updates the UI and prints a message
//          as soon as the program begins and ends
            currentProgramsInterpreter.setFalseProgramExecutionListener(new FalseInterpreter.FalseProgramExecutionListener() {
                @Override
                public void executionStarted() {
                    signalizeExecution(true);
                    System.out.println(Ressources.PROGRAM_EXECUTION_START);
                }

                @Override
                public void executionEnded() {
                    signalizeExecution(false);
                    System.out.println(Ressources.PROGRAM_EXECUTION_END);
                }
            });

//          afterwards we'll start the actual execution on its own thread
            new Thread(() -> {
                try {
                    currentProgramsInterpreter.execute();
                }
//              if there is an interpreterror
                catch (InterpretError interpretError) {
//                  if the user wants every error to be logged
                    if (settings.isToBeLogged(Ressources.LoggingLevels.EVERY)) {
//                      log the error
                        log.logMessage(Ressources.ERROR_EXECUTEPROJECT_INTERPRETERROR);
                        log.logThrowable(interpretError);
                    }

//                  and print the message of the InterpretationError
//                  ending of the program and signalizing the end of the program is handled by the FalseInterpreter
                    System.out.println(interpretError.getMessage());
                }
//              if for any reason there is another kind of exception or error
                catch (Exception | Error exc) {
//                  if the user wants fatal errors to be logged
                    if (settings.isToBeLogged(Ressources.LoggingLevels.FATAL)) {
//                      log the error
                        log.logMessage(Ressources.ERROR_EXECUTEPROJECT_OTHEREXCEPTION);
                        log.logThrowable(exc);
                    }

//                  print the message of the Error and signalize that the program has actually ended
                    System.out.println(Ressources.PROGRAM_EXECUTION_ENDED_AFTER_ERROR);
                    signalizeExecution(false);
                }
            }).start();
        }
//      if the user had to be restricted since there already was a program running we'll tell him that he's not allowed to do that
        else {
            new ProgramAlreadyRunningDialog(this).setVisible(true);
        }
    }

    private void executeProjectWithoutCompiling() {
//      restrict to one program running at a time
        if (!currentlyExecuted) {
//          if we're executing a project from the projects list
            if (lastClickedItem != null) {
//              and the current project is not saved
                if (!currentProjectSaved) {
//                  we'll show an dialog to the user showing him that his progress might be lost if he continued
                    UnsavedProgressDialog unsavedProgress = new UnsavedProgressDialog(this);
                    unsavedProgress.setVisible(true);

//                  if the user doesn't want to proceed we'll end this method
                    if (!unsavedProgress.isToBeChanged()) {
                        return;
                    }
                }

//              if the current project is saved or the user wants to proceed without saving
//              we'll open the project that was clicked
                openProject(lastClickedItem.getLastPathComponent().toString());
            }

//          we'll signalize that we want to compile the currently opened project
            lastClickedItem = null;

            if (!new File(settings.getWorkSpace() + currentProject + File.separator + currentProject + Ressources.BYTE_FILE_ENDING).exists()) {
//              show the user that the project needs to have at least been compiled once
                new NotCompiledExecutionErrorDialog(this).setVisible(true);
//              and stop trying to execute the project
                return;
            }

            try {
//              we'll load the program from he current projects byte code file
                FalseProgram programToExecute = FalseProgram.loadByteCode(settings.getWorkSpace() + currentProject + File.separator + currentProject + Ressources.BYTE_FILE_ENDING);

//              if we may proceed we'll create a interpreter for this program
                currentProgramsInterpreter = new FalseInterpreter(programToExecute);

//              we signalize that we do not want neither stack nor variable contents printed to stdout
                currentProgramsInterpreter.setPrintStack(false);
                currentProgramsInterpreter.setPrintVariables(false);

//              we give the interpreter an executionlistener, that simply updates the UI and prints a message
//              as soon as the program begins and ends
                currentProgramsInterpreter.setFalseProgramExecutionListener(new FalseInterpreter.FalseProgramExecutionListener() {
                    @Override
                    public void executionStarted() {
                        signalizeExecution(true);
                        System.out.println(Ressources.PROGRAM_EXECUTION_START);
                    }

                    @Override
                    public void executionEnded() {
                        signalizeExecution(false);
                        System.out.println(Ressources.PROGRAM_EXECUTION_END);
                    }
                });

//              afterwards we'll start the actual execution on its own thread
                new Thread(() -> {
                    try {
                        currentProgramsInterpreter.execute();
                    }
//                  if there is an interpreterror
                    catch (InterpretError interpretError) {
//                      if the user wants every error to be logged
                        if (settings.isToBeLogged(Ressources.LoggingLevels.EVERY)) {
//                          log the error
                            log.logMessage(Ressources.ERROR_EXECUTEPROJECT_INTERPRETERROR);
                            log.logThrowable(interpretError);
                        }

//                      and print the message of the InterpretationError
//                      ending of the program and signalizing the end of the program is handled by the FalseInterpreter
                        System.out.println(interpretError.getMessage());
                    }
//                  if for any reason there is another kind of exception or error
                    catch (Exception | Error exc) {
//                      if the user wants fatal errors to be logged
                        if (settings.isToBeLogged(Ressources.LoggingLevels.FATAL)) {
//                          log the error
                            log.logMessage(Ressources.ERROR_EXECUTEPROJECT_OTHEREXCEPTION);
                            log.logThrowable(exc);
                        }

//                      print the message of the Error and signalize that the program has actually ended
                        System.out.println(Ressources.PROGRAM_EXECUTION_ENDED_AFTER_ERROR);
                        signalizeExecution(false);
                    }
                }).start();
            } catch (IOException e) {
//              if the user wants moderate errors to be logged
                if (settings.isToBeLogged(Ressources.LoggingLevels.MODERATE)) {
//                  log the error
                    log.logMessage(Ressources.ERROR_EXECUTEPROJECTWITHOUTCOMPILING_IOEXCEPTION);
                    log.logThrowable(e);
                }

//              show the user that there was an error
                new IOErrorDialog(this).setVisible(true);
            }
        }
//      if the user had to be restricted since there already was a program running we'll tell him that he's not allowed to do that
        else {
            new ProgramAlreadyRunningDialog(this).setVisible(true);
        }
    }

    /**
     * This method tries to read the content of an source code file of the project with
     * the given name.
     *
     * @param project the name of the project whose source code file is to be read
     * @return the source code of the project; {@code null} if there was an error
     */
    @Nullable
    private String getFileContent(String project) {
        String content = "";

//      create a BufferedReader for the source code file of the given project
        try (BufferedReader read = new BufferedReader(new FileReader(new File(settings.getWorkSpace() + project + File.separator + project + Ressources.SOURCE_FILE_ENDING)))) {
//          read the files contents
            String temp;

            while ((temp = read.readLine()) != null) {
                content += temp + "\n";
            }
        }
//      if there was an error occurring
        catch (IOException e) {
//          if the user wants fatal errors to be logged
            if (settings.isToBeLogged(Ressources.LoggingLevels.MODERATE)) {
//              log the error
                log.logMessage(Ressources.ERROR_GETFILECONTENT_IOEXCEPTION);
                log.logThrowable(e);
            }

//          Show dialog that the file somehow couldn't be read
            new IOErrorDialog(this).setVisible(true);
//          and return null
            return null;
        }

//      if there was no error we'll return the content of the file
        return content;
    }

    /**
     * This method shows the program whether or not there is now a program executing
     *
     * @param executed whether the program has begun executing (otherwise it will have ended)
     */
    private void signalizeExecution(boolean executed) {
//      we'll save the execution of the program
        currentlyExecuted = executed;
//      and en- / disable the abort-execution-button accordingly
        uiForm.getAbortExecutionButton().setEnabled(currentlyExecuted);
    }

    /**
     * This method aborts the execution of the current program.
     * In case there is no program being executed currently either a {@link NullPointerException} will be thrown
     * or nothing happens.
     */
    private void abortExecution() {
        currentProgramsInterpreter.abortExecution();
    }

    /**
     * This method tries to delete the project that has lastly been clicked.
     */
    private void deleteProject() {
//      we retrieve the projects folder and all the files inside the projects folder
        File projectFolder = new File(settings.getWorkSpace() + lastClickedItem.getLastPathComponent().toString());
        File[] filesToDelete = projectFolder.listFiles();

//      we need to keep track whether any file has not been able to be deleted
        boolean success = filesToDelete != null;

//      we need to delete every file inside the projects folder
        for (int i = 0; success && i < filesToDelete.length; i++) {
            success = filesToDelete[i].delete();
        }

//      to finally delete the folder itself, which we are only able to
//      if every file inside the folder has also been deleted
        if (success) {
            success = projectFolder.delete();
        }

//      Then we show a dialog showing whether the deletion were successful or not
        new DeleteProjectDialog(this, success).setVisible(true);

//      and we refresh the list of projects
        refreshFileTree();
    }

    /**
     * This method tries to open the Error Log in an external application
     * (whichever the user has as default editor for .txt files).<br>
     * If any problem appears during doing so we'll show an dialog saying that the log youldn't be opened.
     */
    private void showLog() {
//      we'll firstly save the log to keep it up to date
        saveLog();

//      if the error log file doesn't exist, meaning it is empty currently
        if (!new File(log.getLogFilePath()).exists()) {
//          we'll show a message that the log couldn't be opened
            new ErrorLogNotOpenableDialog(this).setVisible(true);
            return;
        }

        try {
//          try to open the file with the default application
            Desktop.getDesktop().open(new File(log.getLogFilePath()));
        } catch (IOException e) {
//          if the user wants every error to be logged
            if (settings.isToBeLogged(Ressources.LoggingLevels.EVERY)) {
//              log the error
                log.logMessage(Ressources.ERROR_SHOWLOG_IOEXCEPTION);
                log.logThrowable(e);
            }

//          and show an error that the log couldn't be opened
            new ErrorLogNotOpenableDialog(this).setVisible(true);
        }
    }

    /**
     * This method simply saves the log so that it contains any until now logged error
     */
    private void saveLog() {
        log.writeLog();
    }

    /**
     * This method simply clears the log (internally).
     * The log file will be deleted if the log is saved after calling this method
     */
    private void clearLog() {
        log.clearLog();
    }

    /**
     * This method prompts the user to edit the settings.<br>
     * Also it refreshes the UI according to the new settings.
     */
    private void editSettings() {
//      if the current project is not saved
        if (!currentProjectSaved) {
//          we'll show an dialog to the user showing him that his progress might be lost if he continued
            UnsavedProgressDialog unsavedProgress = new UnsavedProgressDialog(this);
            unsavedProgress.setVisible(true);

//          if the user doesn't want to proceed we'll end this method
            if (!unsavedProgress.isToBeChanged()) {
                return;
            }
        }

//      we'll prompt the user to change the settings
        ChangeSettingsDialog dialog = new ChangeSettingsDialog(this, settings);
        dialog.setVisible(true);

//      and if the settings are possibly changed
        if (dialog.settingsChanged()) {
//          we'll copy the new settings
            settings = dialog.getResultedSettings();
//          close the current project
            closeProject();
//          and refresh the list of projects
            refreshFileTree();
        }
    }

    /**
     * This class is used to return the result of a compilation process easily.<br>
     * This object stores whether the compilation was successful, and if so the resulted program.
     */
    private static class CompilationResult {

        /**
         * The program that resulted from the compilation process (if it was successful)<br>
         */
        @Nullable
        private FalseProgram resultedProgramm;
        /**
         * Stores whether the compilation was successful or not
         */
        private boolean success;

        /**
         * Constructs a compilation result with given values
         *
         * @param pr  the resulted program
         * @param suc whether the compilation was successful
         */
        public CompilationResult(@Nullable FalseProgram pr, boolean suc) {
            this.resultedProgramm = pr;
            this.success = suc;
        }

        /**
         * This method gives you the program that resulted in the compilation process that created this compilation result.<br>
         * If {@link #isSuccess()} returns {@code false} this method should return {@code null}. This constraint though is not checked!
         *
         * @return the resulted program
         */
        @Nullable
        public FalseProgram getResultedProgram() {
            return resultedProgramm;
        }

        /**
         * This method shows whether the compilation has succeeded
         *
         * @return whether the compilation succeeded
         */
        public boolean isSuccess() {
            return success;
        }
    }

    /**
     * The main method that brings everything to roll
     *
     * @param args the command line arguments given to the program
     */
    public static void main(String... args) {
        new FalseIDE();
    }
}
