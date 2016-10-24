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

package de.jeanpierrehotz.falseide.ui.settingsui;

import com.sun.istack.internal.Nullable;
import de.jeanpierrehotz.falseide.ressources.Ressources;
import de.jeanpierrehotz.falseide.ui.DialogCompat;
import de.jeanpierrehotz.falseide.utils.FalseIDESettings;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 *
 */
public class ChangeSettingsDialog extends DialogCompat {

    private static final int MOBILEUSECHECKBOX_WIDTH = 600;
    private static final int MOBILEUSECHECKBOX_HEIGHT = 20;

    private static final int WORKSPACES_INDENT_MARGIN = 30;

    private static final int WORKSPACES_LABEL_WIDTH = 140;
    private static final int WORKSPACES_LABEL_HEIGHT = 20;

    private static final int SELECTRELATIVEFOLDERBUTTON_WIDTH = 140;
    private static final int SELECTRELATIVEFOLDERBUTTON_HEIGHT = 20;

    private static final int RELATIVEWORKSPACE_TEXTFIELD_WIDTH = MOBILEUSECHECKBOX_WIDTH - WORKSPACES_LABEL_WIDTH - WORKSPACES_INDENT_MARGIN - SELECTRELATIVEFOLDERBUTTON_WIDTH - COMPONENT_MARGIN;
    private static final int RELATIVEWORKSPACE_TEXTFIELD_HEIGHT = 20;

    private static final int SELECTABSOLUTEFOLDERBUTTON_WIDTH = 140;
    private static final int SELECTABSOLUTEFOLDERBUTTON_HEIGHT = 20;

    private static final int ABSOLUTEWORKSPACE_TEXTFIELD_WIDTH = MOBILEUSECHECKBOX_WIDTH - WORKSPACES_LABEL_WIDTH - WORKSPACES_INDENT_MARGIN - SELECTABSOLUTEFOLDERBUTTON_WIDTH - COMPONENT_MARGIN;
    private static final int ABSOLUTEWORKSPACE_TEXTFIELD_HEIGHT = 20;

    private static final int LOGGINGLEVEL_CAPTION_WIDTH = MOBILEUSECHECKBOX_WIDTH;
    private static final int LOGGINGLEVEL_CAPTION_HEIGHT = 20;

    private static final int LOGGINGLEVEL_MESSAGE_WIDTH = MOBILEUSECHECKBOX_WIDTH;
    private static final int LOGGINGLEVEL_MESSAGE_HEIGHT = 40;

    private static final int LOGGINGLEVEL_SLIDER_WIDTH = MOBILEUSECHECKBOX_WIDTH;
    private static final int LOGGINGLEVEL_SLIDER_HEIGHT = 20;

    private static final int ABORTBUTTON_WIDTH = 100;
    private static final int ABORTBUTTON_HEIGHT = 20;

    private static final int OKBUTTON_WIDTH = 100;
    private static final int OKBUTTON_HEIGHT = 20;

    private static final int WIDTH = MOBILEUSECHECKBOX_WIDTH + 2 * (MARGIN + OTHER_MARGIN);
    private static final int HEIGHT = TOP_MARGIN + OTHER_MARGIN + 2 * MARGIN + 5 * COMPONENT_MARGIN + MOBILEUSECHECKBOX_HEIGHT + 2 * WORKSPACES_LABEL_HEIGHT
            + LOGGINGLEVEL_CAPTION_HEIGHT + LOGGINGLEVEL_MESSAGE_HEIGHT + LOGGINGLEVEL_SLIDER_HEIGHT;

    private FalseIDESettings    previousSettings;
    private FalseIDESettings    newSettings;

    private boolean             isToBechanged;

    private JCheckBox           mobileUseCheckbox;

    private JLabel              relativeWorkspaceLabel;
    private JTextField          relativeWorkSpacetextField;
    private JButton             selectRelativeFolderButton;

    private JLabel              absoluteWorkSpaceLabel;
    private JTextField          absoluteWorkSpacetextField;
    private JButton             selectAbsoluteFolderButton;

    private JLabel              loggingLevelLabel;
    private JSlider             loggingLevelSlider;
    private JTextPane           loggingLevelMessageTextPane;

    private JButton             okButton;
    private JButton             abortButton;

    private JFileChooser        workSpaceFolderChooser;

    private ItemListener        mobileUseCheckboxItemListener = e -> refreshMobileUse();
    private ChangeListener      loggingLevelSliderChangeListener = e -> refreshLoggingLevelMessage();

    public ChangeSettingsDialog(Frame frame, FalseIDESettings currentSettings) {
        super(frame, Ressources.CHANGESETTINGSDIALOG_CAPTION, true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                actionPerformed(new ActionEvent(abortButton, (int) System.currentTimeMillis(), Ressources.DIALOG_CLOSEACTION));
            }
        });

        previousSettings = currentSettings;
        newSettings = new FalseIDESettings(previousSettings);

        isToBechanged = false;

        setLayout(null);

        workSpaceFolderChooser = new JFileChooser(previousSettings.getAbsoluteWorkSpace());
        workSpaceFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        mobileUseCheckbox = new JCheckBox(Ressources.CHANGESETTINGSDIALOG_MOBILEUSECHECKBOX_CAPTION, previousSettings.isMobileUse());
        mobileUseCheckbox.addItemListener(mobileUseCheckboxItemListener);
        mobileUseCheckbox.setBounds(
                OTHER_MARGIN + MARGIN,
                TOP_MARGIN + MARGIN,
                MOBILEUSECHECKBOX_WIDTH,
                MOBILEUSECHECKBOX_HEIGHT
        );
        add(mobileUseCheckbox);

        relativeWorkspaceLabel = new JLabel(Ressources.CHANGESETTINGSDIALOG_RELATIVEWORKSPACELABEL_CAPTION);
        relativeWorkspaceLabel.setBounds(
                OTHER_MARGIN + MARGIN + WORKSPACES_INDENT_MARGIN,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + COMPONENT_MARGIN,
                WORKSPACES_LABEL_WIDTH,
                WORKSPACES_LABEL_HEIGHT
        );
        add(relativeWorkspaceLabel);

        relativeWorkSpacetextField = new JTextField(previousSettings.getRelativeWorkSpace());
        relativeWorkSpacetextField.setBounds(
                OTHER_MARGIN + MARGIN + WORKSPACES_INDENT_MARGIN + WORKSPACES_LABEL_WIDTH,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + COMPONENT_MARGIN,
                RELATIVEWORKSPACE_TEXTFIELD_WIDTH,
                RELATIVEWORKSPACE_TEXTFIELD_HEIGHT
        );
        add(relativeWorkSpacetextField);

        selectRelativeFolderButton = new JButton(Ressources.CHANGESETTINGSDIALOG_SELECTRELATIVEFOLDERBUTTON_CAPTION);
        selectRelativeFolderButton.addActionListener(this);
        selectRelativeFolderButton.setBounds(
                OTHER_MARGIN + MARGIN + WORKSPACES_INDENT_MARGIN + WORKSPACES_LABEL_WIDTH + RELATIVEWORKSPACE_TEXTFIELD_WIDTH + COMPONENT_MARGIN,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + COMPONENT_MARGIN,
                SELECTRELATIVEFOLDERBUTTON_WIDTH,
                SELECTABSOLUTEFOLDERBUTTON_HEIGHT
        );
        add(selectRelativeFolderButton);

        absoluteWorkSpaceLabel = new JLabel(Ressources.CHANGESETTINGSDIALOG_ABSOLUTEWORKSPACELABEL_CAPTION);
        absoluteWorkSpaceLabel.setBounds(
                OTHER_MARGIN + MARGIN + WORKSPACES_INDENT_MARGIN,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + WORKSPACES_LABEL_HEIGHT + 2 * COMPONENT_MARGIN,
                WORKSPACES_LABEL_WIDTH,
                WORKSPACES_LABEL_HEIGHT
        );
        add(absoluteWorkSpaceLabel);

        absoluteWorkSpacetextField = new JTextField(previousSettings.getAbsoluteWorkSpace());
        absoluteWorkSpacetextField.setBounds(
                OTHER_MARGIN + MARGIN + WORKSPACES_INDENT_MARGIN + WORKSPACES_LABEL_WIDTH,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + WORKSPACES_LABEL_HEIGHT + 2 * COMPONENT_MARGIN,
                ABSOLUTEWORKSPACE_TEXTFIELD_WIDTH,
                ABSOLUTEWORKSPACE_TEXTFIELD_HEIGHT
        );
        add(absoluteWorkSpacetextField);

        selectAbsoluteFolderButton = new JButton(Ressources.CHANGESETTINGSDIALOG_SELECTABSOLUTEFOLDERBUTTON_CAPTION);
        selectAbsoluteFolderButton.addActionListener(this);
        selectAbsoluteFolderButton.setBounds(
                OTHER_MARGIN + MARGIN + WORKSPACES_INDENT_MARGIN + WORKSPACES_LABEL_WIDTH + ABSOLUTEWORKSPACE_TEXTFIELD_WIDTH + COMPONENT_MARGIN,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + WORKSPACES_LABEL_HEIGHT + 2 * COMPONENT_MARGIN,
                SELECTABSOLUTEFOLDERBUTTON_WIDTH,
                SELECTABSOLUTEFOLDERBUTTON_HEIGHT
        );
        add(selectAbsoluteFolderButton);

        loggingLevelLabel = new JLabel(Ressources.CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_CAPTION);
        loggingLevelLabel.setBounds(
                OTHER_MARGIN + MARGIN,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + 2 * WORKSPACES_LABEL_HEIGHT + 3 * COMPONENT_MARGIN,
                LOGGINGLEVEL_CAPTION_WIDTH,
                LOGGINGLEVEL_CAPTION_HEIGHT
        );
        add(loggingLevelLabel);

        loggingLevelSlider = new JSlider(JSlider.HORIZONTAL, Ressources.LoggingLevels.EVERY, Ressources.LoggingLevels.NONE, previousSettings.getLoggingLevel());
        loggingLevelSlider.addChangeListener(loggingLevelSliderChangeListener);
        loggingLevelSlider.setBounds(
                OTHER_MARGIN + MARGIN,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + 2 * WORKSPACES_LABEL_HEIGHT + 4 * COMPONENT_MARGIN + LOGGINGLEVEL_CAPTION_HEIGHT,
                LOGGINGLEVEL_SLIDER_WIDTH,
                LOGGINGLEVEL_SLIDER_HEIGHT
        );
        add(loggingLevelSlider);

        loggingLevelMessageTextPane = new JTextPane();
        loggingLevelMessageTextPane.setEditable(false);
        loggingLevelMessageTextPane.setBackground(this.getBackground());
        loggingLevelMessageTextPane.setBounds(
                OTHER_MARGIN + MARGIN,
                TOP_MARGIN + MARGIN + MOBILEUSECHECKBOX_HEIGHT + 2 * WORKSPACES_LABEL_HEIGHT + 5 * COMPONENT_MARGIN + LOGGINGLEVEL_CAPTION_HEIGHT + LOGGINGLEVEL_SLIDER_HEIGHT,
                LOGGINGLEVEL_MESSAGE_WIDTH,
                LOGGINGLEVEL_MESSAGE_HEIGHT
        );
        add(loggingLevelMessageTextPane);

        okButton = new JButton(Ressources.DIALOG_OK);
        okButton.setBounds(
                WIDTH - OTHER_MARGIN - 2 * COMPONENT_MARGIN - ABORTBUTTON_WIDTH - OKBUTTON_WIDTH,
                HEIGHT - OTHER_MARGIN - COMPONENT_MARGIN - OKBUTTON_HEIGHT,
                OKBUTTON_WIDTH,
                OKBUTTON_HEIGHT
        );
        okButton.addActionListener(this);
        add(okButton);

        abortButton = new JButton(Ressources.DIALOG_ABORT);
        abortButton.setBounds(
                WIDTH - OTHER_MARGIN - COMPONENT_MARGIN - ABORTBUTTON_WIDTH,
                HEIGHT - OTHER_MARGIN - COMPONENT_MARGIN - ABORTBUTTON_HEIGHT,
                ABORTBUTTON_WIDTH,
                ABORTBUTTON_HEIGHT
        );
        abortButton.addActionListener(this);
        add(abortButton);

        refreshMobileUse();
        refreshLoggingLevelMessage();

        setResizable(false);

        setBounds(
                getParent().getX() + (getParent().getWidth() - WIDTH) / 2,
                getParent().getY() + (getParent().getHeight() - HEIGHT) / 2,
                WIDTH, HEIGHT
        );
    }

    private void refreshMobileUse(){
        relativeWorkspaceLabel.setEnabled(mobileUseCheckbox.isSelected());
        relativeWorkSpacetextField.setEnabled(mobileUseCheckbox.isSelected());
        absoluteWorkSpaceLabel.setEnabled(!mobileUseCheckbox.isSelected());
        absoluteWorkSpacetextField.setEnabled(!mobileUseCheckbox.isSelected());
        selectAbsoluteFolderButton.setEnabled(!mobileUseCheckbox.isSelected());
    }

    private void refreshLoggingLevelMessage(){
        String msg;

        switch (loggingLevelSlider.getValue()){
            case Ressources.LoggingLevels.EVERY:
                msg = Ressources.CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_MSG1;
                break;
            case Ressources.LoggingLevels.MODERATE:
                msg = Ressources.CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_MSG2;
                break;
            case Ressources.LoggingLevels.FATAL:
                msg = Ressources.CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_MSG3;
                break;
            case Ressources.LoggingLevels.NONE:
                msg = Ressources.CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_MSG4;
                break;
            default:
                msg = "";
                actionPerformed(new ActionEvent(abortButton, (int) System.currentTimeMillis(), Ressources.DIALOG_CLOSEACTION));
        }

        loggingLevelMessageTextPane.setText(msg);
    }

    public FalseIDESettings getResultedSettings(){
        if(isToBechanged){
            return newSettings;
        }else{
            return previousSettings;
        }
    }

    public boolean settingsChanged(){
        return isToBechanged;
    }

    @Nullable
    public static String createRelativePath(String srcFolder, String dest) {
        if(!new File(srcFolder).isAbsolute() || !new File(dest).isAbsolute()) {
            return null;
        }

        ArrayList<String> srcTokens = new ArrayList<>();
        ArrayList<String> destTokens = new ArrayList<>();

        String relativePath = "";

        int index;

        while((index = srcFolder.indexOf(File.separator)) != -1) {
            srcTokens.add(srcFolder.substring(0, index));
            srcFolder = srcFolder.substring(index + 1);
        }

        if(srcFolder != null && !srcFolder.equals("")) {
            srcTokens.add(srcFolder);
        }

        while((index = dest.indexOf(File.separator)) != -1) {
            destTokens.add(dest.substring(0, index));
            dest = dest.substring(index + 1);
        }

        if(dest != null && !dest.equals("")) {
            destTokens.add(dest);
        }

        if(!srcTokens.get(0).equals(destTokens.get(0))) {
//          return null since we cannot give relative paths to different root folders
            return null;
        }

        while(srcTokens.size() != 0 && destTokens.size() != 0 && srcTokens.get(0).equals(destTokens.get(0))) {
            srcTokens.remove(0);
            destTokens.remove(0);
        }

        for(int i = 0; i < srcTokens.size(); i++) {
            relativePath += ".." + File.separator;
        }

        for(int i = 0; i < destTokens.size(); i++) {
            relativePath += destTokens.get(i);

            if(i != destTokens.size() - 1 || !destTokens.get(i).contains(".")) {
                relativePath += File.separator;
            }
        }

        return relativePath;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if(ev.getSource() == selectAbsoluteFolderButton){
            int res = workSpaceFolderChooser.showOpenDialog(this);

            if(res == JFileChooser.APPROVE_OPTION && workSpaceFolderChooser.getSelectedFile() != null) {
                absoluteWorkSpacetextField.setText(workSpaceFolderChooser.getSelectedFile().getAbsolutePath());
            }

            return;
        } else if(ev.getSource() == selectRelativeFolderButton){
            int res = workSpaceFolderChooser.showOpenDialog(this);

            if(res == JFileChooser.APPROVE_OPTION && workSpaceFolderChooser.getSelectedFile() != null){
                String relativePath = createRelativePath(new File("").getAbsolutePath(), workSpaceFolderChooser.getSelectedFile().getAbsolutePath());

                if(relativePath != null){
                    relativeWorkSpacetextField.setText(relativePath);
                }
            }

            return;
        }

        isToBechanged = ev.getSource() == okButton;

        newSettings.setMobileUse(mobileUseCheckbox.isSelected());
        newSettings.setRelativeWorkSpace(relativeWorkSpacetextField.getText());
        newSettings.setAbsoluteWorkSpace(absoluteWorkSpacetextField.getText());
        newSettings.setLoggingLevel(loggingLevelSlider.getValue());

        this.dispose();
    }
}
