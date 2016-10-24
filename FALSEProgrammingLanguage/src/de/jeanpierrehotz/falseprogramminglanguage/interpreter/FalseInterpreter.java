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

package de.jeanpierrehotz.falseprogramminglanguage.interpreter;

import de.jeanpierrehotz.falseprogramminglanguage.data.ExecutionAbortIndicator;
import de.jeanpierrehotz.falseprogramminglanguage.data.FalseProgram;
import de.jeanpierrehotz.falseprogramminglanguage.data.StackMemory;
import de.jeanpierrehotz.falseprogramminglanguage.data.StandardInOutPutCompat;
import de.jeanpierrehotz.falseprogramminglanguage.data.error.InterpretError;

import java.io.*;
import java.util.HashMap;

/**
 *
 * @author Admin
 */
public class FalseInterpreter {

    private StandardInOutPutCompat stdinout;

    private StackMemory<Object> stack;
    private HashMap<Character, Object> variables;

    private ExecutionAbortIndicator indicator;

    private FalseProgramExecutionListener programExecutionListener;

    private FalseProgram program;

    private boolean printStack;
    private boolean printVariables;

    public void setFalseProgramExecutionListener(FalseProgramExecutionListener listener){
        this.programExecutionListener = listener;
    }

    public void setPrintStack(boolean printStack){
        this.printStack = printStack;
    }

    public void setPrintVariables(boolean printVariables){
        this.printVariables = printVariables;
    }

    public boolean isPrintVariables(){
        return printVariables;
    }

    public FalseInterpreter(FalseProgram program){
        this(program, System.out, System.in);
    }

    public boolean isPrintStack(){
        return printStack;
    }

    public FalseInterpreter(FalseProgram program, PrintStream outStream, InputStream inStream){
        this.stdinout = new StandardInOutPutCompat(inStream, outStream);

        this.program = program;

        this.stack = new StackMemory<>();
        this.variables = new HashMap<>();

        this.printStack = false;
        this.printVariables = false;

        this.indicator = new ExecutionAbortIndicator();
    }

    public StandardInOutPutCompat getStdInOut(){
        return stdinout;
    }

    public void execute() throws InterpretError {
        if(this.programExecutionListener != null){
            this.programExecutionListener.executionStarted();
        }

        InterpretError caughtError = null;

        try {
            executeInternal();
        }catch(InterpretError error){
            caughtError = error;
        }

        if(this.programExecutionListener != null){
            this.programExecutionListener.executionEnded();
        }

        if(caughtError != null && !indicator.isToBeAborted()) {
            throw caughtError;
        }
    }

    private void executeInternal() throws InterpretError {
        program.operate(stack, variables, this, indicator);
    }

    public void abortExecution(){
        indicator.abortExecution();
    }

    public interface FalseProgramExecutionListener{
        void executionStarted();
        void executionEnded();
    }

}
