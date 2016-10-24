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

package de.jeanpierrehotz.falseprogramminglanguage.compiler.error;

import java.util.ArrayList;
import java.util.List;

/**
 * This class may be used by the FalseCompiler to collect any CompileError detected during compilation.<br>
 * It can there after be thrown, whereas the {@link #getMessage()} returns all the messages of the errors collected
 * by the object that you call this mehtod on.<br>
 * You may add any error by calling {@link #addError(CompilationError)} with the Error you want to add as parameter.<br>
 * You may retrieve any error by calling {@link #getError(int)} where {@code 0 <= ind < CompileErrorCollection.size()}.
 */
public class CompileErrorCollection extends Exception {

    /**
     * The list of CompilationErrors collected by this object
     */
    private List<CompilationError> collectedErrors;

    /**
     * This constructor creates a CompileErrorCollection with an empty list of errors
     */
    public CompileErrorCollection(){
        collectedErrors = new ArrayList<>();
    }

    /**
     * This method allows you to add any CompileError to this list
     *
     * @param error the error you want to add
     */
    public void addError(CompilationError error){
        collectedErrors.add(error);
    }

    /**
     * This method gives you the error at given index.
     *
     * @param ind the index of the error you want to retrieve; constraint: {@code 0 <= ind < CompileErrorCollection.size()}
     * @return the error at given index
     * @throws ArrayIndexOutOfBoundsException if the given constraint is not met
     * @see #size()
     */
    public CompilationError getError(int ind){
        return collectedErrors.get(ind);
    }

    /**
     * This method gives you the number of errors collected by this object
     *
     * @return the number of errors currently in this object
     */
    public int size(){
        return collectedErrors.size();
    }

    /**
     * This method gives you the messages of all the errors collected by this object
     *
     * @return the error-messages of all the errors each separated by a line break
     */
    @Override
    public String getMessage() {
        String msg = "";

        for(int i = 0; i < collectedErrors.size(); i++){
            msg +=  (i + 1) + ". " + collectedErrors.get(i).getMessage() + "\n";
        }

        if(msg.endsWith("\n")){
            msg = msg.substring(0, msg.length() - 1);
        }

        return msg;
    }
}
