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

package de.jeanpierrehotz.falseprogramminglanguage.data.operationimplementations;

import de.jeanpierrehotz.falseprogramminglanguage.data.ExecutionAbortIndicator;
import de.jeanpierrehotz.falseprogramminglanguage.data.Operation;
import de.jeanpierrehotz.falseprogramminglanguage.data.StackMemory;
import de.jeanpierrehotz.falseprogramminglanguage.data.error.EmptyStackInterpretError;
import de.jeanpierrehotz.falseprogramminglanguage.data.error.InterpretError;
import de.jeanpierrehotz.falseprogramminglanguage.data.error.WrongDataTypeInterpretError;
import de.jeanpierrehotz.falseprogramminglanguage.interpreter.FalseInterpreter;
import de.jeanpierrehotz.falseprogramminglanguage.language.FalseLanguageDefinition;

import java.util.HashMap;

/**
 * Created by Admin on 17.09.2016.
 */
public class WhileOperation extends Operation {

    public static final byte OPERATION_BYTE = 37;

    public WhileOperation(int index) {
        super(index);
    }

    @Override
    public void operate(StackMemory<Object> stack, HashMap<Character, Object> variables, FalseInterpreter interpreter, ExecutionAbortIndicator indicator)
            throws InterpretError {

        Object boolfunc;
        Object func;

        if(stack.size() >= 2){
            func = stack.pop();
            boolfunc = stack.pop();
        }else{
            throw new EmptyStackInterpretError(index);
        }

        if(! (boolfunc instanceof Function)){
            throw new WrongDataTypeInterpretError("Datatype error: Expected Function; Found " + boolfunc.getClass().toString() + "\nOccured at instruction number " + index);
        }
        if(! (func instanceof Function)){
            throw new WrongDataTypeInterpretError("Datatype error: Expected Function; Found " + func.getClass().toString() + "\nOccured at instruction number " + index);
        }

        while(!indicator.isToBeAborted() && evaluateCondition((Function) boolfunc, stack, variables, interpreter, indicator)){
            ((Function) func).operate(stack, variables, interpreter, indicator);
        }
    }

    private boolean evaluateCondition(Function boolFunc, StackMemory<Object> stack, HashMap<Character, Object> variables, FalseInterpreter interpreter, ExecutionAbortIndicator indicator) throws InterpretError {
        boolFunc.operate(stack, variables, interpreter, indicator);

        Object n;

        if(stack.size() >= 1){
            n = stack.pop();
        }else{
            throw new EmptyStackInterpretError(index);
        }

        if(! (n instanceof Integer)){
            throw new WrongDataTypeInterpretError("Datatype error: Expected Boolean represented by Integer; Found " + n.getClass().toString() + "; Booleans may only contain -1 (true) or 0 (false).\nOccured at instruction number " + index);
        }else if((int) n != FalseLanguageDefinition.TRUE && (int) n != FalseLanguageDefinition.FALSE){
            throw new WrongDataTypeInterpretError("Datatype error: Expected Boolean represented by Integer; Found " + n.getClass().toString() + "; Booleans may only contain -1 (true) or 0 (false).\nOccured at instruction number " + index);
        }

        return (int) n == FalseLanguageDefinition.TRUE;
    }

    @Override
    public Byte[] toByteArray() {
        return new Byte[]{ OPERATION_BYTE };
    }

    @Override
    public String toString() {
        return "#";
    }
}
