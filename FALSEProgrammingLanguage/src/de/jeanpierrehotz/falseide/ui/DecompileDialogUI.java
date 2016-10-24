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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 */
public class DecompileDialogUI {
    private JPanel          contentPanel;

    private JButton         selectFileButton;
    private JTextField      pathTextField;
    private JEditorPane     decompiledCodeEditorPane;
    private JLabel          pathLabel;
    private JScrollPane     decompiledCodeEditorWrapperPane;

    public DecompileDialogUI(){
        decompiledCodeEditorWrapperPane.addMouseWheelListener(ev -> {
            if (ev.isControlDown()) {
                Font baseFont = decompiledCodeEditorPane.getFont();

                setdecompiledEditorFontSize((ev.getUnitsToScroll() > 0) ? Math.max(8, baseFont.getSize() - 1) : Math.min(48, baseFont.getSize() + 1));
            }
        });

        pathLabel.setText(Ressources.DECOMPILEDIALOG_PATHLABEL);
        selectFileButton.setText(Ressources.DECOMPILEDIALOG_SELECTFILEBUTTON);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void setSelectFileButtonListener(ActionListener list) {
        this.selectFileButton.addActionListener(list);
    }

    public void setdecompiledEditorFontSize(int size){
        int usedSize = Math.max(8, Math.min(48, size));

        Font baseFont = decompiledCodeEditorPane.getFont();
        Font newFont = new Font(baseFont.getName(), baseFont.getStyle(), usedSize);
        decompiledCodeEditorPane.setFont(newFont);
    }

    public void selectFile(String path){
        pathTextField.setText(path);
    }

    public void setDecompiledCode(String code){
        decompiledCodeEditorPane.setText(code);
    }
}
