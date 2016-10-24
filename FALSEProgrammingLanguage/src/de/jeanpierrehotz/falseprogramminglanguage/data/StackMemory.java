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

import java.util.ArrayList;

/**
 * This class is a convenience class that is used to resemble a memory stack.
 */
public class StackMemory<E> extends ArrayList<E>{

    /**
     * This method pops the top item off the stack.
     * @return the item that was on top of the stack
     * @throws ArrayIndexOutOfBoundsException if the stack is empty
     */
    public E pop(){
//      store the topmost item
        E toPop = get(size() - 1);
//      remove it from the stack
        remove(size() - 1);
//      and return the item
        return toPop;
    }

    /**
     * This method pushes the given object on top of the stack
     * @param toPush the object to push on top of the stack
     */
    public void push(E toPush){
        add(toPush);
    }

    /**
     * This method picks the item at given position.<br>
     * This means that the item is taken (not removed though!) and added on top of the stack again.<br>
     * For example the stack is 1 2 3 4 and we pick the item at position 2 this results in the stack 1 2 3 4 2.<br>
     * Thus picking the item at position 0 would only duplicate the topmost item
     * @param i the index of the item to pick
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
     */
    public void pick(int i){
//      make sure the index is in bounds
        if(i < 0 || i >= size()){
            throw new ArrayIndexOutOfBoundsException(i);
        }

//      get (not remove!) the item at given index (note the stack begins at the end of the ArrayList)
//      and push it to the stack again
        push(get(size() - 1 - i));
    }

}
