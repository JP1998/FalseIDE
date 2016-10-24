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

package de.jeanpierrehotz.falseprogramminglanguage.language;

/**
 * This class provides some definitions about the FALSE programming language as implemented by this java version.
 */
public class FalseLanguageDefinition {

    //
    // Truthvalues:
    //
    public static final int TRUE = -1;
    public static final int FALSE = 0;

    //
    // Miscellanous:
    //
    public static final char COMMENT_BEGIN = '{';
    public static final char COMMENT_END = '}';

    public static final char LAMBDA_BEGIN = '[';
    public static final char LAMBDA_END = ']';

    public static final char VARIABLE_SCOPE_BEGIN = 'a';
    public static final char VARIABLE_SCOPE_END = 'z';

    public static final char CHARACTER_DECLARATION = '\'';
    /**
     * NOTE:
     * This operator is parsed by the compiler into a operation that simply does nothing.<br>
     * Thus it IS valid to put Assembly into your code, but it won't work!
     * {@link de.jeanpierrehotz.falseprogramminglanguage.data.operationimplementations.NOP}
     */
    public static final char INLINE_ASSEMBLY_DECLARATION = '`';

    //
    // Variable access:
    //
    public static final char ASSIGN_OPERATOR = ':';
    public static final char READ_OPERATOR = ';';
    public static final char EXECUTE_OPERATOR = '!';

    //
    // Arithmetic operators:
    //
    public static final char ADD_OPERATOR = '+';
    public static final char SUBTRACT_OPERATOR = '-';
    public static final char MULTIPLY_OPERATOR = '*';
    public static final char DIVIDE_OPERATOR = '/';
    public static final char UNARYMINUS_OPERATOR = '_';

    //
    // Logic relations operators:
    //
    public static final char EQUALS_OPERATOR = '=';
    public static final char GREATER_OPERATOR = '>';

    public static final char AND_OPERATOR = '&';
    public static final char OR_OPERATOR = '|';
    public static final char NOT_OPERATOR = '~';

    //
    // in-built functions operators:
    //
    public static final char DUPLICATE_OPERATOR = '$';
    public static final char DELETE_OPERATOR = '%';
    public static final char SWAP_OPERATOR = '\\';

    public static final char ROTATE_OPERATOR = '@';
    /**
     * This character can create issues:
     * You can easily produce the character as follows:
     *      in Windows:     ALT + 1 + 5 + 5
     *      or              ALT + 0 + 2 + 4 + 8
     *
     *      in Macintosh:   ALT/&lt;that weird control symbol&gt; + 0
     *
     *      in Linux:       ALT GR + 0
     *      or              COMPOSE, Shift + 7, 0
     *
     * if there is any problem with producing this character or encoding-issues (which you will see from an
     * error-message by he compiler) after reading your source you can use the uppercase O, which
     * is in this version provided as compatibility operator.
     */
    public static final char PICK_OPERATOR = '\u00F8';

    public static final char PICK_OPERATOR_COMPAT = 'O';

    //
    // Control structures:
    //
    public static final char IF_OPERATOR = '?';
    public static final char WHILE_OPERATOR = '#';

    //
    // I/O:
    //
    public static final char PRINT_NUMBER_OPERATION = '.';
    public static final char PRINT_STRING_OPERATION = '\"';
    public static final char PRINT_CHARACTER_OPERATION = ',';
    public static final char READ_CHARACTER_OPERATOR = '^';
    /**
     * This character can create issues:
     * You can easily produce the character somehow
     *
     * if there is any problem with producing this character or encoding-issues (which you will see from an
     * error-message by he compiler) after reading your source you can use the uppercase S, which
     * is in this version provided as compatibility operator.
     */
    public static final char FLUSH_STREAM_OPERATOR = '\u00DF';

    public static final char FLUSH_STREAM_OPERATOR_COMPAT = 'S';

    public static void printReference(){
        System.out.println(
                      "FALSE Language overview\n\n"
                    + "This describes the implementation of the FALSE programming language in Java by\n"
                    + "Jean-Pierre Hotz.\n"
                    + "For further information you can take a look at the FALSE programming language\n"
                    + "manual at http://strlen.com/false/false.txt\n\n"
                    + "-- Truth values ----------------------------------------------------------------\n"
                    + TRUE +                          "          Boolean value of true\n"
                    + FALSE +                         "           Boolean value of false\n"
                    + "-- Miscellaneous ---------------------------------------------------------------\n"
                    + COMMENT_BEGIN + " comment " + COMMENT_END + " Comment\n"
                    + LAMBDA_BEGIN + " code " + LAMBDA_END + "    Lambda / function\n"
                    + VARIABLE_SCOPE_BEGIN + " ... " + VARIABLE_SCOPE_END + "     variable scope\n"
                    + CHARACTER_DECLARATION +         "           character declaration\n"
                    + INLINE_ASSEMBLY_DECLARATION +   "           inline assemby (is ignored!)\n"
                    + "\n-- Variable access -------------------------------------------------------------\n"
                    + ASSIGN_OPERATOR +               "           assign value to variable\n"
                    + READ_OPERATOR +                 "           reads variable\n"
                    + EXECUTE_OPERATOR +              "           executes the lambda expression at the top of the stack\n"
                    + "\n-- Arithmetic operators --------------------------------------------------------\n"
                    + ADD_OPERATOR +                  "           addition operator\n"
                    + SUBTRACT_OPERATOR +             "           subtraction operator\n"
                    + MULTIPLY_OPERATOR +             "           multiplication operator\n"
                    + DIVIDE_OPERATOR +               "           division operator\n"
                    + UNARYMINUS_OPERATOR +           "           unary minus operator\n"
                    + "\n-- Logic relations operators ---------------------------------------------------\n"
                    + EQUALS_OPERATOR +               "           equals operator\n"
                    + GREATER_OPERATOR +              "           greater than operator\n"
                    + AND_OPERATOR +                  "           logical and operator\n"
                    + OR_OPERATOR +                   "           logical or operator\n"
                    + NOT_OPERATOR +                  "           logical not operator\n"
                    + "\n-- In-built functions operators ------------------------------------------------\n"
                    + DUPLICATE_OPERATOR +            "           duplicate topmost stackitem\n"
                    + DELETE_OPERATOR +               "           delete topmost stackitem\n"
                    + SWAP_OPERATOR +                 "           swap the two topmost stackitems\n"
                    + ROTATE_OPERATOR +               "           rotates the three topmost stackitems\n"
                    + PICK_OPERATOR +                 "           pick: copies the nth stackitem to the top of the stack\n"
                    + PICK_OPERATOR_COMPAT +          "           alt. pick operator - use when having issues with encoding\n"
                    +                                "            or when having trouble with producing the character"
                    + "\n-- Control structures ----------------------------------------------------------\n"
                    + IF_OPERATOR +                   "           condition; if-equivalent\n"
                    + WHILE_OPERATOR +                "           loop; while-equivalent\n"
                    + "\n-- I/O -------------------------------------------------------------------------\n"
                    + PRINT_NUMBER_OPERATION +        "           prints the topmost stackitem as number\n"
                    + PRINT_STRING_OPERATION + "string" + PRINT_STRING_OPERATION +        "    prints the string\n"
                    + PRINT_CHARACTER_OPERATION +     "           prints the topmost stackitem as character\n"
                    + READ_CHARACTER_OPERATOR +       "           reads a character from the buffer into the stack\n"
                    + FLUSH_STREAM_OPERATOR +         "           flush: flushes the stdin and stdout stream\n"
                    + FLUSH_STREAM_OPERATOR_COMPAT +  "           alt. flush operator - use when having issues with encoding\n"
                    +                                "            or when having trouble with producing the character"
        );
    }
}
