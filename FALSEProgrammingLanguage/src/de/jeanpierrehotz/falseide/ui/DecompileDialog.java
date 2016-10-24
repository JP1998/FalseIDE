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

package de.jeanpierrehotz.falseide.ui;

import de.jeanpierrehotz.falseide.ressources.Ressources;
import de.jeanpierrehotz.falseide.utils.ErrorLogger;
import de.jeanpierrehotz.falseide.utils.FalseIDESettings;
import de.jeanpierrehotz.falseprogramminglanguage.data.FalseProgram;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 *
 */
public class DecompileDialog extends Dialog {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private DecompileDialogUI ui;

    private JFileChooser fileChooser;

    private ErrorLogger log;
    private FalseIDESettings settings;

    public DecompileDialog(Frame frame, ErrorLogger log, FalseIDESettings settings) {
        super(frame, Ressources.DECOMPILEDIALOG_CAPTION, true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        this.ui = new DecompileDialogUI();
        add(this.ui.getContentPanel());

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(new FileNameExtensionFilter(Ressources.BYTECODE_FILEEXTENSION_DESCRIPTION, Ressources.BYTECODE_FILEEXTENSION));
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        this.log = log;
        this.settings = settings;

        this.ui.setSelectFileButtonListener(ev -> {
            int res = fileChooser.showSaveDialog(DecompileDialog.this);

            if(res == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null){

                String selectedByteCodeFile = fileChooser.getSelectedFile().getAbsolutePath();

                ui.selectFile(selectedByteCodeFile);

                try {
                    FalseProgram decompiled = FalseProgram.loadByteCode(selectedByteCodeFile);
                    ui.setDecompiledCode(decompiled.decompile());
                } catch (IOException e) {
                    if(settings.isToBeLogged(Ressources.LoggingLevels.MODERATE)){
                        log.logMessage(Ressources.ERROR_DECOMPILEPROJECT_IOEXCEPTION);
                        log.logThrowable(e);
                    }

                    ui.setDecompiledCode(Ressources.DECOMPILEPROJECT_ERRORMESSAGE);
                }
            }
        });

        setBounds(
                (getParent().getWidth() - DEFAULT_WIDTH) / 2,
                (getParent().getHeight() - DEFAULT_HEIGHT) / 2,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT
        );
    }
}
