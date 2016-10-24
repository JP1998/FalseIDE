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

import de.jeanpierrehotz.falseprogramminglanguage.data.error.InterpretError;
import de.jeanpierrehotz.falseprogramminglanguage.interpreter.FalseInterpreter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This method resembles a group of Operations that are stored inside one operation
 */
public abstract class OperationGroup extends Operation {

    /**
     * The instructions that are resembled by this group
     */
    protected ArrayList<Operation> instructions;

    /**
     * This constructor can be used to initialize the instruction list
     * @param instr the list of instructions resembled by this group
     */
    public OperationGroup(ArrayList<Operation> instr) {
        super(-1);
        this.instructions = instr;
    }

    @Override
    public void operate(StackMemory<Object> stack, HashMap<Character, Object> variables, FalseInterpreter interpreter, ExecutionAbortIndicator indicator) throws InterpretError {
//      this method operates as long as we have not worked through every instruction and as long as this program is not to be aborted
        for(int i = 0; i < instructions.size() && !indicator.isToBeAborted(); i++){
//          we'll simply operate the instruction at the given index
            instructions.get(i).operate(stack, variables, interpreter, indicator);

//          if needed print the stack
            if(interpreter.isPrintStack())
                printStack(stack);

//          and if needed print the variables
            if(interpreter.isPrintVariables())
                printVariables(variables);
        }
    }

    /**
     * This method prints given Stack to the stdout printstream
     * @param stack the stack to print
     */
    public void printStack(StackMemory<Object> stack){
        int tabsize = 10;

        String content = "";

        System.out.println("Stack[size=" + stack.size() + ";]");
        for(int i = 0; i < stack.size(); i++){
            content += stack.get(i);

            while(content.length() < (i + 1) * tabsize){
                content += " ";
            }
        }
        System.out.println(content);
    }

    /**
     * This method prints given variables to the stdout printstream
     * @param variables the variables to print
     */
    public void printVariables(HashMap<Character, Object> variables){
        int tabsize = 10;

        String content = "";
        int ctr = 0;

        System.out.println("Variables");
        for(Character c : variables.keySet()){
            content += c + "=" + variables.get(c);

            ctr++;

            while(content.length() < (ctr + 1) * tabsize){
                content += " ";
            }
        }
        System.out.println(content);
    }

}
