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

/**
 * Created by Admin on 01.10.2016.
 */
public class ExecutionAbortIndicator {

    private boolean toBeAborted;

    public ExecutionAbortIndicator(){
        toBeAborted = false;
    }

    public void abortExecution(){
        this.toBeAborted = true;
    }

    public boolean isToBeAborted() {
        return toBeAborted;
    }
}
