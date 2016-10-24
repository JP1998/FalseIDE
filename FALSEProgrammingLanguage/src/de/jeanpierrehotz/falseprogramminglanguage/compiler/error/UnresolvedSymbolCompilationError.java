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
import de.jeanpierrehotz.falseprogramminglanguage.language.FalseLanguageDefinition;

/**
 * This class may be thrown by the {@link FalseCompiler}-class
 * in case that there is a symbol that is unknown in the FALSE programming language as defined in the
 * {@link FalseLanguageDefinition}.
 */
public class UnresolvedSymbolCompilationError extends CompilationError {

    /**
     * This constructor creates the UnresolvedSymbolCompilationError for the token at the index pos, which is shown
     * in the snippet codeSnippet.
     *
     * @param pos the position of the token that is not known
     * @param codeSnippet the code snippet that shows the token that is not known
     */
    public UnresolvedSymbolCompilationError(int pos, String codeSnippet){
        super("Unresolved symbol at index " + pos + ": " + codeSnippet);
    }

    /**
     * This constructor creates the UnresolvedSymbolCompilationError for the token at the index pos, which is shown
     * in the snippet codeSnippet.
     *
     * @param pos the position of the token that is not known
     * @param codeSnippet the code snippet that shows the token that is not known
     * @param additionalInfo the additional info about the token that is not known
     */
    public UnresolvedSymbolCompilationError(int pos, String codeSnippet, String additionalInfo){
        super("Unresolved symbol at index " + pos + ": " + codeSnippet + "\n" + additionalInfo);
    }

}
