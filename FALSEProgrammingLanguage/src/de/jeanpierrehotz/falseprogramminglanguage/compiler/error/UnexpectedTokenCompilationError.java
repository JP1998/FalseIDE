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

import de.jeanpierrehotz.falseprogramminglanguage.compiler.FalseCompiler;

/**
 * This class may be thrown by the {@link FalseCompiler}-class
 * in case that there is a token that is not expected. This may be a closing lambda-function token without an opening token
 * or something similar.
 */
public class UnexpectedTokenCompilationError extends CompilationError {

    /**
     * This constructor creates the UnexpectedTokenCompilationError for the token at the index pos, which is shown
     * in the snippet codeSnippet.
     *
     * @param pos the position of the token that has not been expected
     * @param codeSnippet the code snippet that shows the token that has not been expected
     */
    public UnexpectedTokenCompilationError(int pos, String codeSnippet){
        super("Unexpected symbol at index " + pos + ": " + codeSnippet);
    }

    /**
     * This constructor creates the UnexpectedTokenCompilationError for the token at the index pos, which is shown
     * in the snippet codeSnippet. It also gives the additional info provided by you.
     *
     * @param pos the position of the token that has not been expected
     * @param codeSnippet the code snippet that shows the token that has not been expected
     * @param additionalInfo the additional info about the token that has not been expected
     */
    public UnexpectedTokenCompilationError(int pos, String codeSnippet, String additionalInfo){
        super("Unexpected symbol at index " + pos + ": " + codeSnippet + "\n" + additionalInfo);
    }

}
