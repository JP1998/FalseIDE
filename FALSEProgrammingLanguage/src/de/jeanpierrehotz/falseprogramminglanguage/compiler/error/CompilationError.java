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
 * in case there is any mistake in the code that is supposed to be compiled.<br>
 * To specify what kind of CompilationError it is you should extend this Exception.
 */
public class CompilationError extends Exception {

    public CompilationError(String s) {
        super(s);
    }

}
