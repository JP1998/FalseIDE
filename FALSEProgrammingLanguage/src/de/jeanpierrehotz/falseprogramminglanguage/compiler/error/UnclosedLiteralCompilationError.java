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
 * in case that there is a literal such as a comment, a lambda-function or a string, that has not been properly been closed.
 */
public class UnclosedLiteralCompilationError extends CompilationError {

    /**
     * This constructor creates the UnclosedLiteralCompilationError for the token at the index pos, which is shown
     * in the snippet codeSnippet.
     *
     * @param pos the position of the token that has not been closed
     * @param codeSnippet the code snippet that shows the token that has not been closed
     */
    public UnclosedLiteralCompilationError(int pos, String codeSnippet){
        super("Literal at position " + pos + " has not been closed: " + codeSnippet);
    }

}
