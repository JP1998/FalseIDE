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
import de.jeanpierrehotz.falseprogramminglanguage.data.error.InterpretError;
import de.jeanpierrehotz.falseprogramminglanguage.interpreter.FalseInterpreter;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by Admin on 17.09.2016.
 */
public class IntegerValueOperation extends Operation {

    public static final byte OPERATION_BYTE = 11;

    private int value;

    public IntegerValueOperation(int index, int value) {
        super(index);
        this.value = value;
    }

    @Override
    public void operate(StackMemory<Object> stack, HashMap<Character, Object> variables, FalseInterpreter interpreter, ExecutionAbortIndicator indicator)
            throws InterpretError {
        stack.push(value);
    }

    @Override
    public Byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(value).position(0);

        Byte[] returnValue = new Byte[5];
        returnValue[0] = OPERATION_BYTE;

        for(int i = 0; i < 4; i++){
            returnValue[1 + i] = bb.get();
        }

        return returnValue;
    }


    @Override
    public String toString() {
        return getValueWithWhiteSpace(true);
    }

    public String getValueWithWhiteSpace(boolean ws){
        return ((ws)? " ": "") + value;
    }
}
