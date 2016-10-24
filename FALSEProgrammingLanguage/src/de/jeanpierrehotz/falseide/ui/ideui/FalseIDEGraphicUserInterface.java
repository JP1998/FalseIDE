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

package de.jeanpierrehotz.falseide.ui.ideui;

import de.jeanpierrehotz.falseide.ressources.Ressources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class FalseIDEGraphicUserInterface {

    private JPanel contentPanel;
    private JTree fileTree;

    private JTextPane currentFileEditor;
    private JTextPane consoleEditor;
    private JButton abortExecutionButton;
    private JLabel consoleLabel;
    private JLabel projectTreeLabel;
    private JScrollPane fileEditorWrapperScrollPane;
    private JScrollPane consoleEditorWrapperScrollPane;

    private PrintStream originalSTDOUT;

    public FalseIDEGraphicUserInterface(){
        originalSTDOUT = System.out;

        System.setOut(new PrintStream(new ConsoleEditorPanePrintStream()));
        System.setIn(new ConsoleEditorPaneInputStream());

        fileTree.setModel(null);

        abortExecutionButton.setToolTipText(Ressources.ABORTBUTTON_TOOLTIP);
        consoleLabel.setText(Ressources.CONSOLE_LABEL);
        projectTreeLabel.setText(Ressources.PROJECTTREE_LABEL);

        fileEditorWrapperScrollPane.addMouseWheelListener(ev -> {
            if (ev.isControlDown()) {
                Font baseFont = currentFileEditor.getFont();

                setFileEditorFontSize((ev.getUnitsToScroll() > 0) ? Math.max(8, baseFont.getSize() - 1) : Math.min(48, baseFont.getSize() + 1));
            }
        });

        consoleEditorWrapperScrollPane.addMouseWheelListener(ev -> {
            if (ev.isControlDown()) {
                Font baseFont = consoleEditor.getFont();

                setConsoleFontSize((ev.getUnitsToScroll() > 0) ? Math.max(8, baseFont.getSize() - 1) : Math.min(48, baseFont.getSize() + 1));
            }
        });
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JTree getFileTree(){
        return fileTree;
    }

    public void setFileEditorText(String text){
        currentFileEditor.setText(text);
    }

    public String getFileEditorText(){
        return currentFileEditor.getText();
    }

    public void setFileEditorCaretPosition(int pos){
        currentFileEditor.setCaretPosition(pos);
    }

    public int getFileEditorCaretPosition(){
        return currentFileEditor.getCaretPosition();
    }

    public void addFileEditorKeyListener(KeyListener list){
        currentFileEditor.addKeyListener(list);
    }

//    public JTextPane getFileEditorPane(){
//        return currentFileEditor;
//    }

//    public JTextPane getConsoleEditorPane() {
//        return consoleEditor;
//    }

    public int getFileEditorFontSize(){
        return currentFileEditor.getFont().getSize();
    }

    public void setFileEditorFontSize(int size){
        int usedSize = Math.max(8, Math.min(48, size));

        Font baseFont = currentFileEditor.getFont();
        Font newFont = new Font(baseFont.getName(), baseFont.getStyle(), usedSize);
        currentFileEditor.setFont(newFont);
    }

    public int getConsoleFontSize(){
        return consoleEditor.getFont().getSize();
    }

    public void setConsoleFontSize(int size){
        int usedSize = Math.max(8, Math.min(48, size));

        Font baseFont = consoleEditor.getFont();
        Font newFont = new Font(baseFont.getName(), baseFont.getStyle(), usedSize);
        consoleEditor.setFont(newFont);
    }

    public JButton getAbortExecutionButton(){
        return abortExecutionButton;
    }

    public void clearConsole(){
        consoleEditor.setText("");
    }

    private class ConsoleEditorPanePrintStream extends OutputStream {

        @Override
        public void write(int i) throws IOException {
            originalSTDOUT.write(i);
            if(i != 13) {
                consoleEditor.setText(consoleEditor.getText() + ((char) i));
            }

            consoleEditor.setCaretPosition(consoleEditor.getDocument().getLength());
        }

        @Override
        public void write(byte[] bytes, int off, int len) throws IOException {
            if(bytes == null){
                throw new NullPointerException();
            } else if((off < 0) || (off > bytes.length) || (len < 0) || ((off + len) > bytes.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if(len == 0){
                return;
            }

            String text = new String(bytes, off, len);

            originalSTDOUT.print(text);
            consoleEditor.setText(consoleEditor.getText() + text);

            consoleEditor.setCaretPosition(consoleEditor.getDocument().getLength());
        }
    }

    private class ConsoleEditorPaneInputStream extends InputStream {

        private LinkedBlockingQueue<Character> blockingQueue;

        public ConsoleEditorPaneInputStream(){
            consoleEditor.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if(!e.isControlDown() && !e.isAltDown() && !e.isAltGraphDown()) {
                        blockingQueue.offer(e.getKeyChar());
                        consoleEditor.setCaretPosition(consoleEditor.getDocument().getLength());
                    }
                }

                @Override
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE){
                        e.consume();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e){}
            });

            blockingQueue = new LinkedBlockingQueue<>();
        }

        @Override
        public int read() throws IOException {
            int c = -1;

            try {
                c = blockingQueue.take();
            }catch (InterruptedException exc){
                exc.printStackTrace();
            }

            return c;
        }

        @Override
        public int read(byte[] bytes, int off, int len) throws IOException {
            if(bytes == null){
                throw new NullPointerException();
            }
            if(off < 0 || len < 0){
                throw new IndexOutOfBoundsException();
            }
            if(len == 0){
                return 0;
            }

            int i = 0;
            byte currentItem;

            do{
                currentItem = (byte) read();

                if(currentItem == -1){
                    break;
                }

                bytes[off + i++] = currentItem;

            } while(i < len && currentItem != '\n');

            return i;
        }
    }
}
