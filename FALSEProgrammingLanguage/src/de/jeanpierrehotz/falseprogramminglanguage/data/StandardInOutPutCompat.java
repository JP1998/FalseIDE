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

package de.jeanpierrehotz.falseprogramminglanguage.data;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * This class is used by the {@link de.jeanpierrehotz.falseprogramminglanguage.interpreter.FalseInterpreter}-class to
 * make usage of other in- and outputstreams than the standard in and out possible with False programs.
 */
public class StandardInOutPutCompat {

    /**
     * The scanner that scans text from the given inputstream
     */
    private Scanner input;
    /**
     * The printstream the program should print to
     */
    private PrintStream output;

    /**
     * The buffer that contains the input since the last flushing
     */
    private String inputBuffer;

    /**
     * This constructor creates a {@link StandardInOutPutCompat}-object for given in and output streams
     * @param inStream the input stream to use with the program
     * @param outStream the output stream to use with the program
     */
    public StandardInOutPutCompat(InputStream inStream, PrintStream outStream){
        this.input = new Scanner(inStream);
        this.output = outStream;

        this.inputBuffer = "";
    }

    /**
     * This constructor creates a {@link StandardInOutPutCompat}-object with {@link System#in}
     * and {@link System#out} as the in and output streams that are to be used.
     */
    public StandardInOutPutCompat(){
        this(System.in, System.out);
    }

    /**
     * This method writes the given String to the output stream
     * @param content the text to print
     */
    public void writeString(String content){
        output.println(content);
    }

    /**
     * This method writes the given number to the output stream
     * @param num the number to print
     */
    public void writeNumber(int num){
        output.println(num);
    }

    /**
     * This method writes the given character to the output stream
     * @param character the character to print
     */
    public void writeCharacter(char character){
        output.println(character);
    }

    /**
     * This method reads a character from the current buffer.<br>
     * If this buffer is empty this method will return {@code -1}. Otherwise
     * it will return the integer value of the next character.
     */
    public int readCharacter(){
//      if the buffer is not empty
        if(!inputBuffer.equals("")){
//          trim the buffer and return the next character
            char c = inputBuffer.charAt(0);
            inputBuffer = inputBuffer.substring(1);
            return (int) c;
        }
//      otherwise (if the buffer is empty)
        else{
//          we'll return -1
            return -1;
        }
    }

    /**
     * This method flushes the outputstream and reads a new line into the buffer from the input stream
     */
    public void flush(){
        output.flush();
        inputBuffer = input.nextLine();
    }
}
