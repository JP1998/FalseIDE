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

import java.nio.ByteBuffer;

/**
 * Created by Admin on 17.09.2016.
 */
public class AssemblyOperation extends NOP {

    public static final byte OPERATION_BYTE = 12;

    private int assemblyOperation;

    public AssemblyOperation(int index, int assemblyOperation) {
        super(index);
        this.assemblyOperation = assemblyOperation;
    }

    @Override
    public Byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(assemblyOperation).position(0);

        Byte[] returnValue = new Byte[5];
        returnValue[0] = OPERATION_BYTE;

        for(int i = 0; i < 4; i++){
            returnValue[1 + i] = bb.get();
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return " " + assemblyOperation + "`";
    }
}
