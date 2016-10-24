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

import de.jeanpierrehotz.falseprogramminglanguage.data.Operation;
import de.jeanpierrehotz.falseprogramminglanguage.data.OperationGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin on 17.09.2016.
 */
public class Function extends OperationGroup {

    public static final byte OPERATION_BEGIN_BYTE = 8;
    public static final byte OPERATION_END_BYTE = 126;

    public Function(ArrayList<Operation> instr) {
        super(instr);
    }

//    @Override
//    public String toString() {
//        return "Function";
//    }

    @Override
    public Byte[] toByteArray() {
        ArrayList<Byte> codeList = new ArrayList<>();

        codeList.add(OPERATION_BEGIN_BYTE);

        for(int i = 0; i < instructions.size(); i++){
            codeList.addAll(Arrays.asList(instructions.get(i).toByteArray()));
        }

        codeList.add(OPERATION_END_BYTE);

        Byte[] returnValue = new Byte[codeList.size()];
        for(int i = 0; i < codeList.size(); i++){
            returnValue[i] = codeList.get(i);
        }

        return returnValue;
    }


    @Override
    public String toString() {
        String content = "[";

        for(int i = 0; i < instructions.size(); i++){
            Operation op = instructions.get(i);

            if(!(op instanceof IntegerValueOperation)){
                content += op.toString();
            }else{
                content += ((IntegerValueOperation) op).getValueWithWhiteSpace(i > 0 && instructions.get(i - 1) instanceof IntegerValueOperation);
            }
        }

        content += "]";

        return content;
    }
}
