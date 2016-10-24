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

package de.jeanpierrehotz.falseide.ui.maindialogs;

import de.jeanpierrehotz.falseide.ressources.Ressources;
import de.jeanpierrehotz.falseide.ui.DialogCompat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 */
public class CreateProjectDialog extends DialogCompat {

    private static final int NAMELABEL_WIDTH = 60;
    private static final int NAMELABEL_HEIGHT = 20;

    private static final int NAMETEXTFIELD_WIDTH = 140;
    private static final int NAMETEXTFIELD_HEIGHT = 20;

    private static final int OKBUTTON_WIDTH = 100;
    private static final int OKBUTTON_HEIGHT = 20;

    private static final int ABORTBUTTON_WIDTH = 100;
    private static final int ABORTBUTTON_HEIGHT = 20;

    private static final int WIDTH = 2 * (MARGIN + OTHER_MARGIN) + NAMELABEL_WIDTH + NAMETEXTFIELD_WIDTH;
    private static final int HEIGHT = TOP_MARGIN + OTHER_MARGIN + ((NAMELABEL_HEIGHT > NAMETEXTFIELD_HEIGHT)? NAMELABEL_HEIGHT: NAMETEXTFIELD_HEIGHT) + 2 * MARGIN;


    private boolean toBeCreated;
    private String projectName;

    private JLabel nameLabel;
    private JTextField nameTextField;

    private JButton abortButton;
    private JButton okButton;

    public CreateProjectDialog(Frame frame) {
        super(frame, Ressources.CREATEPROJECTDIALOG_CAPTION, true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                actionPerformed(new ActionEvent(abortButton, (int) System.currentTimeMillis(), Ressources.DIALOG_CLOSEACTION));
            }
        });

        toBeCreated = false;
        projectName = "";

        setLayout(null);

        nameLabel = new JLabel(Ressources.CREATEPROJECTDIALOG_NAME_CAPTION);
        nameLabel.setBounds(OTHER_MARGIN + MARGIN, TOP_MARGIN + MARGIN, NAMELABEL_WIDTH, NAMELABEL_HEIGHT);
        add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(OTHER_MARGIN + MARGIN + NAMELABEL_WIDTH, TOP_MARGIN + MARGIN, NAMETEXTFIELD_WIDTH, NAMETEXTFIELD_HEIGHT);
        nameTextField.addActionListener(this);
        add(nameTextField);

        okButton = new JButton(Ressources.DIALOG_OK);
        okButton.setBounds(WIDTH - OTHER_MARGIN - 2 * COMPONENT_MARGIN - ABORTBUTTON_WIDTH - OKBUTTON_WIDTH, HEIGHT - OTHER_MARGIN - COMPONENT_MARGIN - OKBUTTON_HEIGHT, OKBUTTON_WIDTH, OKBUTTON_HEIGHT);
        okButton.addActionListener(this);
        add(okButton);

        abortButton = new JButton(Ressources.DIALOG_ABORT);
        abortButton.setBounds(WIDTH - OTHER_MARGIN - COMPONENT_MARGIN - ABORTBUTTON_WIDTH, HEIGHT - OTHER_MARGIN - COMPONENT_MARGIN - ABORTBUTTON_HEIGHT, ABORTBUTTON_WIDTH, ABORTBUTTON_HEIGHT);
        abortButton.addActionListener(this);
        add(abortButton);

        setResizable(false);

        setBounds(
                getParent().getX() + (getParent().getWidth() - WIDTH) / 2,
                getParent().getY() + (getParent().getHeight() - HEIGHT) / 2,
                WIDTH, HEIGHT
        );
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        toBeCreated = evt.getSource() == okButton || evt.getSource() == nameTextField;
        projectName = nameTextField.getText();
        this.dispose();
    }

    public boolean isToBeCreated() {
        return toBeCreated;
    }

    public String getProjectName() {
        return projectName;
    }
}