/*
 *    Copyright 2016 Jean-Pierre Hotz
 *    
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.jeanpierrehotz.falseide.ressources.languageindependence;

import java.awt.Color;
import java.util.Locale;

/**
 * Created by the program Java Ressource Generator built by Jean-Pierre Hotz
 * This class is (together with the class R and its subclasses) created to prevent hardcoding and stuff like that
 * This class provides methods to access the values stored int the R-class via the ids stored and accessible
 * in its static subclasses.
 */
public class Context{

    /**
     * This Locale contains the Locale this Java-Runtime executes on.
     * With this Locale we determine, which String we have to return in the {@link Context#getString(int)}-method
     */
    static final Locale RUNTIME_LOCALE;
    static{
        RUNTIME_LOCALE = Locale.getDefault();
    }

    /**
     * This method gives you the string with given id in the runtime-language (or if not provided in english)
     * of the system. If the id is not valid this method will return null.
     * @param id the id (accessible in class {@link R.string})
     * @return the string in system-dependent language
     */
    public static final String getString(int id){
        if (id < 0) return null;

        if(RUNTIME_LOCALE.getDisplayLanguage().equals(Locale.ENGLISH.getDisplayLanguage()) && id < Strings.en.length){
            return Strings.en[id];
        }else if(RUNTIME_LOCALE.getDisplayLanguage().equals(Locale.GERMAN.getDisplayLanguage()) && id < Strings.de.length){
            return Strings.de[id];
        }else if (id < Strings.en.length){
            return Strings.en[id];
        }else{
            return null;
        }
    }

    /**
     * This method gives you the constant value with given id. If the id is invalid this mehtod will return -1
     * @param id the id (accessible in class {@link R.integer})
     * @return the value
     */
    public static final int getInt(int id){
        if (id >= 0 && id < Integers.values.length)
            return Integers.values[id];
        else
            return -1;
    }

    /**
     * This method gives you the color with given id. If the given id is invalid this method will return null
     * @param id the id (accessible in class {@link R.color})
     * @return the color
     */
    public static final Color getColor(int id){
        if (id >= 0 && id < Colors.values.length)
            return new Color(Colors.values[id], true);
        else
            return null;
    }
}
