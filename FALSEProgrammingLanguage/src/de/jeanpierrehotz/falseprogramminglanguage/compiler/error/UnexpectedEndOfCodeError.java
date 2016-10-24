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
 * This Exception may be thrown by the {@link FalseCompiler}-class
 * in case it reaches the end of the code that is supposed to be
 * compiled although there is a instruction expected.<br>
 */
public class UnexpectedEndOfCodeError extends CompilationError {

    /**
     * This constructor creates the UnexpectedEndOfCodeError for the token, which is shown
     * in the snippet codeSnippet.
     *
     * @param codeSnippet the code snippet that shows the token that has not been expected
     */
    public UnexpectedEndOfCodeError(String codeSnippet){
        super("Unexpected end of code: " + codeSnippet);
    }

    /**
     * This constructor creates the UnexpectedEndOfCodeError for the token, which is shown
     * in the snippet codeSnippet. It also gives the additional info provided by you.
     *
     * @param codeSnippet the code snippet that shows the token that has not been expected
     * @param additionalInfo the additional info about the token that has not been expected
     */
    public UnexpectedEndOfCodeError(String codeSnippet, String additionalInfo){
        super("Unexpected end of code: " + codeSnippet + "\n" + additionalInfo);
    }

}
