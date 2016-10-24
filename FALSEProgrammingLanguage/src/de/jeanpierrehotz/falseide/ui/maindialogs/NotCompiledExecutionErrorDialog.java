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
public class NotCompiledExecutionErrorDialog extends DialogCompat {

    private static final int WIDTH = 396;
    private static final int HEIGHT = 188;

    private static final int MESSAGELABEL_WIDTH = WIDTH - 2 * (MARGIN + OTHER_MARGIN);
    private static final int MESSAGELABEL_HEIGHT = 60;

    private static final int OKBUTTON_WIDTH = 100;
    private static final int OKBUTTON_HEIGHT = 20;


    private JTextPane messageLabel;

    private JButton okButton;

    public NotCompiledExecutionErrorDialog(Frame frame) {
        super(frame, Ressources.NOTCOMPILEDEXECUTIONERRORDIALOG_CAPTION, true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                actionPerformed(new ActionEvent(okButton, (int) System.currentTimeMillis(), Ressources.DIALOG_CLOSEACTION));
            }
        });

        setLayout(null);

        messageLabel = new JTextPane();
        messageLabel.setText(Ressources.NOTCOMPILEDEXECUTIONERRORDIALOG_MESSAGE);
        messageLabel.setEditable(false);
        messageLabel.setBackground(this.getBackground());
        messageLabel.setBounds(MARGIN + OTHER_MARGIN, TOP_MARGIN + MARGIN, MESSAGELABEL_WIDTH, MESSAGELABEL_HEIGHT);
        add(messageLabel);

        okButton = new JButton(Ressources.DIALOG_OK);
        okButton.setBounds(WIDTH - OTHER_MARGIN - COMPONENT_MARGIN - OKBUTTON_WIDTH, HEIGHT - OTHER_MARGIN - COMPONENT_MARGIN - OKBUTTON_HEIGHT, OKBUTTON_WIDTH, OKBUTTON_HEIGHT);
        okButton.addActionListener(this);
        add(okButton);

        setResizable(false);

        setBounds(
                getParent().getX() + (getParent().getWidth() - WIDTH) / 2,
                getParent().getY() + (getParent().getHeight() - HEIGHT) / 2,
                WIDTH, HEIGHT
        );
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.dispose();
    }
}
