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

import java.util.HashMap;

/**
 * Created by Admin on 17.09.2016.
 */
public class UnaryMinusOperation extends Operation {

    public static final byte OPERATION_BYTE = 21;

    public UnaryMinusOperation(int index) {
        super(index);
    }

    @Override
    public void operate(StackMemory<Object> stack, HashMap<Character, Object> variables, FalseInterpreter interpreter, ExecutionAbortIndicator indicator)
            throws InterpretError {

        Object n;

        if(stack.size() >= 1){
            n = stack.pop();
        }else{
            throw new EmptyStackInterpretError(index);
        }

        if(! (n instanceof Integer)){
            throw new WrongDataTypeInterpretError("Datatype error: Expected Integer; Found " + n.getClass().toString() + "\nOccured at instruction number " + index);
        }

        stack.push(- (int) n);
    }

    @Override
    public Byte[] toByteArray() {
        return new Byte[]{ OPERATION_BYTE };
    }

    @Override
    public String toString() {
        return "_";
    }
}
