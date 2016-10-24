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

import de.jeanpierrehotz.falseprogramminglanguage.compiler.FalseCompiler;
import de.jeanpierrehotz.falseprogramminglanguage.data.operationimplementations.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class can be used to create a program in false code.
 */
public class FalseProgram extends OperationGroup {

    /**
     * The byte code
     */
    public static final byte PROGRAM_BEGIN = 7,
            PROGRAM_END = 127;

    public FalseProgram(ArrayList<Operation> instr) {
        super(instr);
    }

    @Override
    public Byte[] toByteArray() {
        ArrayList<Byte> codeList = new ArrayList<>();

        codeList.add(PROGRAM_BEGIN);

        for(int i = 0; i < instructions.size(); i++){
            codeList.addAll(Arrays.asList(instructions.get(i).toByteArray()));
        }

        codeList.add(PROGRAM_END);

        Byte[] returnValue = new Byte[codeList.size()];
        for(int i = 0; i < codeList.size(); i++){
            returnValue[i] = codeList.get(i);
        }

        return returnValue;
    }

    public byte[] toPrimitiveByteArray(){
        Byte[] data = toByteArray();

        byte[] returnValue = new byte[data.length];
        for(int i = 0; i < data.length; i++){
            returnValue[i] = data[i];
        }

        return returnValue;
    }

    public void saveByteCode(String file) throws IOException {
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(toPrimitiveByteArray());
        outStream.close();
    }

    public String decompile(){
        return decompile(this);
    }

    public static String decompile(FalseProgram prog){
        return prog.toString();
    }

    public static FalseProgram loadByteCode(String file) throws IOException {

        byte[] data = Files.readAllBytes(Paths.get(file));

        if(data[0] != PROGRAM_BEGIN || data[data.length - 1] != PROGRAM_END){
            throw new IOException("The byte code has been illegaly changed!");
        }

        return new FalseProgram(parseByteCode(data));
    }

    private static ArrayList<Operation> parseByteCode(byte[] data) throws IOException {
        return parseByteCode(1, data.length - 1, data, new FalseCompiler.OccurrenceCounter());
    }

    private static ArrayList<Operation> parseByteCode(int beg, int end, byte[] data, FalseCompiler.OccurrenceCounter ctr) throws IOException {
        ArrayList<Operation> operations = new ArrayList<>();

        int temp_beg;
        int temp_end;

        int level;

        ByteBuffer bb;

        for(int i = beg; i < end && i < data.length; i++){
            switch (data[i]){
                case Function.OPERATION_BEGIN_BYTE:
                    boolean string = false;

                    temp_beg = i + 1;
                    temp_end = temp_beg;

                    level = 0;

                    while(temp_end < data.length && (data[temp_end] != Function.OPERATION_END_BYTE || string || level > 0)){
                        if(data[temp_end] == Function.OPERATION_BEGIN_BYTE){
                            level++;
                        }else if(data[temp_end] == Function.OPERATION_END_BYTE){
                            level--;
                        }else if(!string && data[temp_end] == PrintStringOperation.OPERATION_BEGIN_BYTE){
                            string = true;
                        }else if(string && data[temp_end] == PrintStringOperation.OPERATION_END_BYTE){
                            string = false;
                        }
                        temp_end++;
                    }

                    operations.add(new LambdaFunctionOperation(ctr.count(), parseByteCode(temp_beg, temp_end, data, ctr)));

                    i = temp_end;

                    break;
                case PrintStringOperation.OPERATION_BEGIN_BYTE:
                    String content = "";

                    temp_beg = i + 1;
                    temp_end = temp_beg;

                    while(temp_end < data.length && data[temp_end] != PrintStringOperation.OPERATION_END_BYTE){
                        bb = ByteBuffer.allocate(2);
                        bb.put(data[temp_end]).put(data[temp_end + 1]).position(0);
                        content += bb.getChar();

                        temp_end += 2;
                    }

                    operations.add(new PrintStringOperation(ctr.count(), content));
                    i = temp_end;

                    break;

                case VariableAdressOperation.OPERATION_BYTE:
                    i++;
                    operations.add(new VariableAdressOperation(ctr.count(), (char) data[i]));
                    break;
                case CharacterValueOperation.OPERATION_BYTE:
                    i++;

                    bb = ByteBuffer.allocate(2);
                    bb.put(data[i++]).put(data[i]).position(0);

                    operations.add(new CharacterValueOperation(ctr.count(), bb.getChar()));

                    break;
                case IntegerValueOperation.OPERATION_BYTE:
                case AssemblyOperation.OPERATION_BYTE:
                    boolean integer = data[i] == IntegerValueOperation.OPERATION_BYTE;
                    i++;

                    bb = ByteBuffer.allocate(4);
                    bb.put(data[i++]).put(data[i++]).put(data[i++]).put(data[i]).position(0);

                    if(integer) {
                        operations.add(new IntegerValueOperation(ctr.count(), bb.getInt()));
                    }else{
                        operations.add(new AssemblyOperation(ctr.count(), bb.getInt()));
                    }

                    break;
                case AssignOperation.OPERATION_BYTE:
                    operations.add(new AssignOperation(ctr.count()));
                    break;
                case ReadOperation.OPERATION_BYTE:
                    operations.add(new ReadOperation(ctr.count()));
                    break;
                case ExecutionOperation.OPERATION_BYTE:
                    operations.add(new ExecutionOperation(ctr.count()));
                    break;
                case AddOperation.OPERATION_BYTE:
                    operations.add(new AddOperation(ctr.count()));
                    break;
                case SubtractOperation.OPERATION_BYTE:
                    operations.add(new SubtractOperation(ctr.count()));
                    break;
                case MultiplyOperation.OPERATION_BYTE:
                    operations.add(new MultiplyOperation(ctr.count()));
                    break;
                case DivideOperation.OPERATION_BYTE:
                    operations.add(new DivideOperation(ctr.count()));
                    break;
                case UnaryMinusOperation.OPERATION_BYTE:
                    operations.add(new UnaryMinusOperation(ctr.count()));
                    break;
                case EqualsOperation.OPERATION_BYTE:
                    operations.add(new EqualsOperation(ctr.count()));
                    break;
                case GreaterThanOperation.OPERATION_BYTE:
                    operations.add(new GreaterThanOperation(ctr.count()));
                    break;
                case AndOperation.OPERATION_BYTE:
                    operations.add(new AndOperation(ctr.count()));
                    break;
                case OrOperation.OPERATION_BYTE:
                    operations.add(new OrOperation(ctr.count()));
                    break;
                case NotOperation.OPERATION_BYTE:
                    operations.add(new NotOperation(ctr.count()));
                    break;
                case DuplicateOperation.OPERATION_BYTE:
                    operations.add(new DuplicateOperation(ctr.count()));
                    break;
                case DeleteOperation.OPERATION_BYTE:
                    operations.add(new DeleteOperation(ctr.count()));
                    break;
                case SwapOperation.OPERATION_BYTE:
                    operations.add(new SwapOperation(ctr.count()));
                    break;
                case RotateOperation.OPERATION_BYTE:
                    operations.add(new RotateOperation(ctr.count()));
                    break;
                case PickOperation.OPERATION_BYTE:
                    operations.add(new PickOperation(ctr.count()));
                    break;
                case IfOperation.OPERATION_BYTE:
                    operations.add(new IfOperation(ctr.count()));
                    break;
                case WhileOperation.OPERATION_BYTE:
                    operations.add(new WhileOperation(ctr.count()));
                    break;
                case PrintNumberOperation.OPERATION_BYTE:
                    operations.add(new PrintNumberOperation(ctr.count()));
                    break;
                case FlushOperation.OPERATION_BYTE:
                    operations.add(new FlushOperation(ctr.count()));
                    break;
                case ReadCharacterOperation.OPERATION_BYTE:
                    operations.add(new ReadCharacterOperation(ctr.count()));
                    break;
                case PrintCharacterOperation.OPERATION_BYTE:
                    operations.add(new PrintCharacterOperation(ctr.count()));
                    break;
                default:
                    throw new IOException("The byte code has been illegaly changed!");
            }
        }

        return operations;
    }

    @Override
    public String toString() {
        String content = "";

        for(int i = 0; i < instructions.size(); i++){
            Operation op = instructions.get(i);

            if(!(op instanceof IntegerValueOperation)){
                content += op.toString();
            }else{
                content += ((IntegerValueOperation) op).getValueWithWhiteSpace(i > 0 && instructions.get(i - 1) instanceof IntegerValueOperation);
            }
        }

        return content;
    }
}
