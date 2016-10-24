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
public class PrintStringOperation extends Operation {

    public static final byte OPERATION_BEGIN_BYTE = -1;
    public static final byte OPERATION_END_BYTE = -2;

    private String content;

    public PrintStringOperation(int index, String content) {
        super(index);
        this.content = content;
    }

    @Override
    public void operate(StackMemory<Object> stack, HashMap<Character, Object> variables, FalseInterpreter interpreter, ExecutionAbortIndicator indicator)
            throws InterpretError {

        interpreter.getStdInOut().writeString(content);
    }

    @Override
    public Byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(2 * content.length());

        for(char c: content.toCharArray()){
            bb.putChar(c);
        }

        Byte[] returnValue = new Byte[2 * (content.length() + 1)];
        returnValue[0] = OPERATION_BEGIN_BYTE;

        byte[] values = bb.array();

        for(int i = 0; i < values.length; i++){
            returnValue[1 + i] = values[i];
        }

        returnValue[returnValue.length - 1] = OPERATION_END_BYTE;

        return returnValue;
    }

    @Override
    public String toString() {
        return "\"" + content + "\"";
    }
}
