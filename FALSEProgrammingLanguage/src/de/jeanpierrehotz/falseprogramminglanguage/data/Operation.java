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

import java.util.HashMap;

/**
 * This class is the abstract super class for every instruction used in the False programming language
 */
public abstract class Operation {

    /**
     * Should be shown when an error is thrown!
     */
    protected int index;

    /**
     * This constructor initializes only the index of the operation
     * @param index the index this operation has occurred in the code
     */
    public Operation(int index){
        this.index = index;
    }

    /**
     * This method operates the Operation, where it has access to the given stack, variables and the programs interpreter.<br>
     * If the Operations operate-method executes a fair amount of time you should note the {@link ExecutionAbortIndicator} that
     * indicates when to abort the execution.
     * @param stack the current stack of the program
     * @param variables the current variables available in the program
     * @param interpreter the interpreter of the program
     * @param indicator the indicator that shows whether to abort the program or not
     * @throws InterpretError if there is an determined error while executing the operation
     */
    public abstract void operate(StackMemory<Object> stack, HashMap<Character, Object> variables, FalseInterpreter interpreter, ExecutionAbortIndicator indicator) throws InterpretError;

    /**
     * This method creates a array of bytes that resembles this operation in byte code.
     * @return the operation resembled in byte code
     */
    public abstract Byte[] toByteArray();

}
