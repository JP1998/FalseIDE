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

package de.jeanpierrehotz.falseprogramminglanguage.compiler;

import de.jeanpierrehotz.falseprogramminglanguage.compiler.error.*;
import de.jeanpierrehotz.falseprogramminglanguage.data.FalseProgram;
import de.jeanpierrehotz.falseprogramminglanguage.data.Operation;
import de.jeanpierrehotz.falseprogramminglanguage.data.operationimplementations.*;
import de.jeanpierrehotz.falseprogramminglanguage.interpreter.FalseInterpreter;
import de.jeanpierrehotz.falseprogramminglanguage.language.FalseLanguageDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can be used to compile FALSE code into a {@link FalseProgram}-object, which can be executed by a
 * {@link FalseInterpreter}-object, which you'll have to pass the {@link FalseProgram} to.
 */
public class FalseCompiler {

    /**
     * We don't want anyone to instantiate this class, since it acts in a static context only.
     */
    private FalseCompiler(){}

    /**
     * This method compiles the given source code into a List of operations interpretable by a {@link FalseInterpreter}
     * object. Therefore the compiler will check your code for any mistakes done (such as uncosed literals, unexpected tokens or unresolved symbols in your code), and will
     * give the first error that occurs while compiling your code as an {@link CompilationError}.
     *
     * @param code The code you want to be compiled into a interpretable list of operations
     * @return the list of interpretable operations generated by your code
     * @throws CompileErrorCollection in case there is a mistake in your code;
     *                                the thrown exception contains a record of all the detected errors in your code
     *
     * @see CompilationError
     * @see UnclosedLiteralCompilationError
     * @see UnexpectedTokenCompilationError
     * @see UnresolvedSymbolCompilationError
     */
    public static FalseProgram compile(String code) throws CompileErrorCollection {
//      first make sure that the code is syntactically correct (if not this method will throw an exception)
        findSyntaxError(code);
//      then free the code from all its unneccessary whitespace
        String compilableCode = strip(code);
//      last but not least parse the code, create a FalseProgram from the Operation-objects and return the created program
        return new FalseProgram(parse(compilableCode));
    }

    /**
     * This mehtod parses every token inside the given code into single {@link Operation}-objects. To do so the code is assumed to be
     * completely correct (thus tested by {@link #findSyntaxError(String)}) and freed from unnecessary whitespace (with {@link #strip(String)}).<br>
     * This method will create a {@link OccurrenceCounter}-object and pass
     * this object as well as the given code to {@link #parse(String, OccurrenceCounter)}.
     *
     * @param code The stripped and completely correct FALSE code to be parsed
     * @return The list of {@link Operation}-objects according to given code
     */
    private static ArrayList<Operation> parse(String code){
//      simply create a new OccurrenceCounter, pass this OccurrenceCounter and the code to the method #parse(String, OccurrenceCounter)
//      and return the result from this call
        return parse(code, new OccurrenceCounter());
    }

    /**
     * This method parses every token inside the given code into single {@link Operation}-objects, where their indices are computed by the given
     * {@link OccurrenceCounter}-object.<br>
     * The code is assumed to be completely correct (thus tested by {@link #findSyntaxError(String)}) and
     * freed from unnecessary whitespace (with {@link #strip(String)}).<br>
     * This method will recursively parse any lambda functions.
     *
     * @param code The stripped and completely correct FALSE code to be parsed
     * @param ctr The {@link OccurrenceCounter} that is used for the current parse process
     * @return The list of {@link Operation}-objects according to given code
     */
    private static ArrayList<Operation> parse(String code, OccurrenceCounter ctr){
        ArrayList<Operation> program = new ArrayList<>();

//      for strings and lambda functions we'll need the beginning and the end of the token
        int beg;
        int end;

//      we'll need the level of functions to evaluate the actual closing-bracket of a lambda function
        int level;

//      we'll iterate over every character in the given code
        for(int i = 0; i < code.length(); i++){
//          in case that there is a lambda function
            if(code.charAt(i) == FalseLanguageDefinition.LAMBDA_BEGIN){
//              we'll mark the beginning and the next possible end of the function
                beg = i;
                end = i + 1;

//              also reset the level
                level = 0;

//              then we'll search for the end of the function by tracking the level of the function we're currently in
//              and iterate over anything that is in a higher level than 0 and is not the end of the function
                while(code.charAt(end) != FalseLanguageDefinition.LAMBDA_END || level > 0){
                    if(code.charAt(end) == FalseLanguageDefinition.LAMBDA_BEGIN){
                        level++;
                    }else if(code.charAt(end) == FalseLanguageDefinition.LAMBDA_END){
                        level--;
                    }

                    end++;
                }

//              then we'll count the index of the LambdaFunctionOperation, and give it all the Operations inside
//              the function by recursively calling #parse(Strign OccurrenceCounter), while only giving it the code
//              inside of the function
                program.add(new LambdaFunctionOperation(ctr.count(), parse(code.substring(beg + 1, end), ctr)));

//              lastly we'll jump over the function token
                i = end;
            }
//          in case that there is a string
            else if(code.charAt(i) == FalseLanguageDefinition.PRINT_STRING_OPERATION){
//              we'll mark the beginning and the next possible end of the string
                beg = i;
                end = i + 1;

//              then we'll search for the end of the string
                while(code.charAt(end) != FalseLanguageDefinition.PRINT_STRING_OPERATION){
                    end++;
                }

//              then we'll count the index of the PrintStringOperation, and give it the String inside the quotes
                program.add(new PrintStringOperation(ctr.count(), code.substring(beg + 1, end)));

//              lastly we'll jump over the string token
                i = end;
            }
//          in case that the current token is in the variable scope
            else if(code.charAt(i) >= FalseLanguageDefinition.VARIABLE_SCOPE_BEGIN
                    && code.charAt(i) <= FalseLanguageDefinition.VARIABLE_SCOPE_END){
//              then we'll count the index of the VariableAdressOperation and give it the variable adressat the current token
                program.add(new VariableAdressOperation(ctr.count(), code.charAt(i)));
            }
//          in case that there is a number beginning at the current token
            else if(isNumerical(code.charAt(i))){
//              we'll mark the beginning and the next possible end of the number
                beg = i;
                end = i;

//              then we'll search for the end of the number
                while(end < code.length() && isNumerical(code.charAt(end))){
                    end++;
                }

//              if there is an inline assembly operator after the number
                if(code.length() >= end + 2 && code.charAt(end + 1) == FalseLanguageDefinition.INLINE_ASSEMBLY_DECLARATION){
//                  then we'll count the index of the AssemblyOperation, and give it the number that we have to parse
                    program.add(new AssemblyOperation(ctr.count(), Integer.parseInt(code.substring(beg, end))));
                }
//              if there is no inline assembly operator after the number
                else{
//                  then we'll count the index of the IntegerValueOperation, and give it the number that we have to parse
                    program.add(new IntegerValueOperation(ctr.count(), Integer.parseInt(code.substring(beg, end))));
                }

//              lastly we'll jump over the number (in case there's a inline assembly operator: it will be ignored)
                i = end - 1;
            }
//          in case that there is a character declaration
            else if(code.charAt(i) == FalseLanguageDefinition.CHARACTER_DECLARATION){
//              we'll jump to the next token
                i++;
//              and store the value of the character inside a CharacterValueOperation
                program.add(new CharacterValueOperation(ctr.count(), code.charAt(i)));
            }

            /*
            For any other defined operator we'll just find the according Operation, instantiate this Operation with the counted index given,
            and add this Operation to the program
             */

            else if(code.charAt(i) == FalseLanguageDefinition.ASSIGN_OPERATOR){
                program.add(new AssignOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.READ_OPERATOR){
                program.add(new ReadOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.EXECUTE_OPERATOR){
                program.add(new ExecutionOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.ADD_OPERATOR){
                program.add(new AddOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.SUBTRACT_OPERATOR){
                program.add(new SubtractOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.MULTIPLY_OPERATOR){
                program.add(new MultiplyOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.DIVIDE_OPERATOR){
                program.add(new DivideOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.UNARYMINUS_OPERATOR){
                program.add(new UnaryMinusOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.EQUALS_OPERATOR){
                program.add(new EqualsOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.GREATER_OPERATOR){
                program.add(new GreaterThanOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.AND_OPERATOR){
                program.add(new AndOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.OR_OPERATOR){
                program.add(new OrOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.NOT_OPERATOR){
                program.add(new NotOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.DUPLICATE_OPERATOR){
                program.add(new DuplicateOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.DELETE_OPERATOR){
                program.add(new DeleteOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.SWAP_OPERATOR){
                program.add(new SwapOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.ROTATE_OPERATOR){
                program.add(new RotateOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.PICK_OPERATOR
                    || code.charAt(i) == FalseLanguageDefinition.PICK_OPERATOR_COMPAT){
                program.add(new PickOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.IF_OPERATOR){
                program.add(new IfOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.WHILE_OPERATOR){
                program.add(new WhileOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.PRINT_NUMBER_OPERATION){
                program.add(new PrintNumberOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.PRINT_CHARACTER_OPERATION){
                program.add(new PrintCharacterOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.READ_CHARACTER_OPERATOR){
                program.add(new ReadCharacterOperation(ctr.count()));
            }else if(code.charAt(i) == FalseLanguageDefinition.FLUSH_STREAM_OPERATOR
                    || code.charAt(i) == FalseLanguageDefinition.FLUSH_STREAM_OPERATOR_COMPAT){
                program.add(new FlushOperation(ctr.count()));
            }
        }

//      after we have parsed the complete code we'll return the list with all the operations in it
        return program;
    }

    /**
     * This method will find any detectable syntax errors in the given code and throw an according {@link CompilationError}.<br>
     * This exception will provide you as much information as possible and will hopefully help you to fix any problems with your code.
     *
     * @param code the code whose syntax has to be checked
     * @throws CompileErrorCollection asf
     */
    private static void findSyntaxError(String code) throws CompileErrorCollection {
        /*
        We need to keep track of following things:

           - all the errors detected by this method (var: errorStack)
           - whether we are inside a string (var: string)
           - what level of a function we currently are in (since there are nested functions) (var: function)
             <level=0 some code here>[<level=1 some other code IN the function here>[<level=2 some code>]<level=1>[<level=2>]<level=1>]<level=0>
           - whether we are inside a comment (var: comment)
           - where the last multi-character token has begun (to provide the index in case it has not been closed)
             they may also provide the index of the closing character, since we keep track whether we are inside such a token
             with independent variables
             (vars:
                strings index: lastString;
                functions: lastOpenedFunctionIndices (List because there are nested functions and we need to keep track of every level of functions);
                comments: lastOpenedComment)
         */

        CompileErrorCollection errorStack = new CompileErrorCollection();

        boolean string = false;
        int lastString = 0;
        int function = 0;
        List<Integer> lastOpenedFunctionIndices = new ArrayList<>();

        boolean comment = false;
        int lastOpenedComment = 0;

//      we iterate over every character inside the code
        for(int i = 0; i < code.length(); i++){
//          we ignore everything inside a comment that is not the closing character for the comment
//          and everything inside a print string statement that is not the closing quote for the string
            if((!comment || code.charAt(i) == FalseLanguageDefinition.COMMENT_END) && (!string || code.charAt(i) == FalseLanguageDefinition.PRINT_STRING_OPERATION)) {
//              we'll decide what to do depending on the character at the current index
                switch (code.charAt(i)) {
                    case FalseLanguageDefinition.COMMENT_BEGIN:
//                      if there's a opening bracket for a comment we'll signalize that we're inside a comment, and store the current index
//                      as the last index where a comment has been opened
                        comment = true;
                        lastOpenedComment = i;
                        break;
                    case FalseLanguageDefinition.COMMENT_END:
                        if(comment) {
//                          if there is a closing bracket for a comment and we are inside a comment currently we'll signalize that we
//                          no longer are inside a comment
                            comment = false;
                            break;
                        }else{
//                          if there is a closing bracket for a comment and we are not inside a comment currently we'll show that the closing bracket
//                          has not been expected
                            errorStack.addError(new UnexpectedTokenCompilationError(i, evaluateCodeSnippet(i, code)));
                        }
                    case FalseLanguageDefinition.CHARACTER_DECLARATION:
//                      if there is a character declaration we'll simply jump over the value declarated by this token
                        i++;
                        break;
                    case FalseLanguageDefinition.PRINT_STRING_OPERATION:
//                      if there is a double-quote we'll toggle the state of being inside a string, and store the current index as
//                      the last index where a string has begun
                        lastString = i;
                        string = !string;
                        break;
                    case FalseLanguageDefinition.LAMBDA_BEGIN:
//                      if we begin a new function we'll increment the level of the current function and store the current index as the last
//                      opening index of a function
                        function++;
                        lastOpenedFunctionIndices.add(i);

                        break;
                    case FalseLanguageDefinition.LAMBDA_END:
//                      if there is a closing function bracket we'll decrement the current function level
                        function--;

//                      if possible we'll remove the last index of unclosed opening function brackets
//                      (the only case this might not be possible is when function < 0 is met -> in this case there will be a error thrown
                        if(lastOpenedFunctionIndices.size() > 0) {
                            lastOpenedFunctionIndices.remove(lastOpenedFunctionIndices.size() - 1);
                        }

//                      if the current function level is less than zero, which indicates that there is a closing function bracket without
//                      an according opening function bracket
                        if (function < 0) {
                            // we'll reset the function levels
                            function = 0;
                            // we'll indicate that this token has not been expected
                            errorStack.addError(new UnexpectedTokenCompilationError(i, evaluateCodeSnippet(i, code)));
                        }

                        /*
                        Most other defined operators will be ignored.
                        They have to be listed though since otherwise they'll fall under the default case
                         */
                    case FalseLanguageDefinition.INLINE_ASSEMBLY_DECLARATION:

                    case FalseLanguageDefinition.ASSIGN_OPERATOR:
                    case FalseLanguageDefinition.READ_OPERATOR:
                    case FalseLanguageDefinition.EXECUTE_OPERATOR:

                    case FalseLanguageDefinition.ADD_OPERATOR:
                    case FalseLanguageDefinition.SUBTRACT_OPERATOR:
                    case FalseLanguageDefinition.MULTIPLY_OPERATOR:
                    case FalseLanguageDefinition.DIVIDE_OPERATOR:
                    case FalseLanguageDefinition.UNARYMINUS_OPERATOR:

                    case FalseLanguageDefinition.EQUALS_OPERATOR:
                    case FalseLanguageDefinition.GREATER_OPERATOR:

                    case FalseLanguageDefinition.AND_OPERATOR:
                    case FalseLanguageDefinition.OR_OPERATOR:
                    case FalseLanguageDefinition.NOT_OPERATOR:

                    case FalseLanguageDefinition.DUPLICATE_OPERATOR:
                    case FalseLanguageDefinition.DELETE_OPERATOR:
                    case FalseLanguageDefinition.SWAP_OPERATOR:
                    case FalseLanguageDefinition.ROTATE_OPERATOR:
                    case FalseLanguageDefinition.PICK_OPERATOR:
                    case FalseLanguageDefinition.PICK_OPERATOR_COMPAT:

                    case FalseLanguageDefinition.IF_OPERATOR:
                    case FalseLanguageDefinition.WHILE_OPERATOR:

                    case FalseLanguageDefinition.PRINT_CHARACTER_OPERATION:
                    case FalseLanguageDefinition.PRINT_NUMBER_OPERATION:
                    case FalseLanguageDefinition.READ_CHARACTER_OPERATOR:

                    case FalseLanguageDefinition.FLUSH_STREAM_OPERATOR:
                    case FalseLanguageDefinition.FLUSH_STREAM_OPERATOR_COMPAT:

                        break;
                    default:
//                      if the current character is not explicitly defined
//                      and it is
//                         1. not inside the variable scope AND
//                         2. not numerical AND
//                         3. not whitespace
                        if (    (!(code.charAt(i) >= FalseLanguageDefinition.VARIABLE_SCOPE_BEGIN
                                && code.charAt(i) <= FalseLanguageDefinition.VARIABLE_SCOPE_END)
                                && !isNumerical(code.charAt(i)) && !Character.isWhitespace(code.charAt(i)))) {

//                          we'll indicate that the current character is not defined as a operator in FALSE
                            errorStack.addError(new UnresolvedSymbolCompilationError(i, evaluateCodeSnippet(i, code)));

                        }
//                      if the current character is inside the variable scope
                        else if(code.charAt(i) >= FalseLanguageDefinition.VARIABLE_SCOPE_BEGIN
                                && code.charAt(i) <= FalseLanguageDefinition.VARIABLE_SCOPE_END){

//                             we'll search for a assigning or reading operator directly after the variable address
//                             thus we first store the next possible index where this operator might be
                            int nextToken = i + 1;

//                          and search for the first index after the variable address that is not white space
                            while(nextToken < code.length() && Character.isWhitespace(code.charAt(nextToken))){
                                nextToken++;
                            }

//                          and if the operator after the variable address is neither reading operator nor assigning operator
                            if(nextToken < code.length() && code.charAt(nextToken) != FalseLanguageDefinition.READ_OPERATOR && code.charAt(nextToken) != FalseLanguageDefinition.ASSIGN_OPERATOR){
//                              we'll signalize that there must be an assigning or reading operator after a variable address
                                errorStack.addError(new UnexpectedTokenCompilationError(nextToken, evaluateCodeSnippet(nextToken, code), "There must be an assigning or an reading operator following a variable adress!"));
                            }
//                          otherwise if we reach the end of the code unexpectedly
                            else if(nextToken >= code.length()){
//                              we'll signalize that the compiler unexpectedly reached the end of the code
                                errorStack.addError(new UnexpectedEndOfCodeError(evaluateCodeSnippet(i, code), "There must be an assigning or an reading operator following a variable adress!"));
                            }
                        }
                }
            }
        }

//      if we're still inside a string at the end of the code
        if(string){
//          we'll signalize that to the user
            errorStack.addError(new UnclosedLiteralCompilationError(lastString, evaluateCodeSnippet(lastString, code)));
        }

//      if we're still inside a string at the end of the code
        if(comment){
//          we'll signalize that to the user
            errorStack.addError(new UnclosedLiteralCompilationError(lastOpenedComment, evaluateCodeSnippet(lastOpenedComment, code)));
        }

//      if we're still at least inside of one function
        if(function > 0){
            for(int token : lastOpenedFunctionIndices){
//              we'll signalize that to the user for every function that has not been closed yet
                errorStack.addError(new UnclosedLiteralCompilationError(token, evaluateCodeSnippet(token, code)));
            }
        }

//      if there is a error that has been detected we'll throw the collection as one single exception containing all the errors detected by this method
        if(errorStack.size() > 0){
            throw errorStack;
        }
    }

    /**
     * This method strips the given code so that it can be easily parsed and
     * checked.<br>
     * It removes any comment and unneccessary whitespace. The only whitespace that is left is the whitespace between numbers and the whitespace inside Strings.
     *
     * @param code The code that should be stripped
     * @return the stripped code
     */
    private static String strip(String code) {
        int ind = 0;
        boolean string = false;
        boolean comment = false;
        int comment_beg = 0;
        int comment_end;

//      we'll go through every character in the code
        while (ind < code.length()) {
//          if we're in a comment
            if (comment) {
//              we'll ignore everything except the comment-ending
                if (code.charAt(ind) == FalseLanguageDefinition.COMMENT_END) {
//                  if there's a comment-ending we'll strip the comment set the index to before the comment
//                  since the next character went to the index of the comment-beginning
//                  and we'll signalize that we're not in a comment anymore
                    comment = false;
                    comment_end = ind;

                    code = code.substring(0, comment_beg) + code.substring(comment_end + 1);

                    ind -= (comment_end - comment_beg) + 1;
                }
            }
//          if we're not in a comment but in a string
            else if (string) {
//              we'll ignore anything but a String border
//              and if there is a String border we'll signalize that we're no longer in a String
                if (code.charAt(ind) == FalseLanguageDefinition.PRINT_STRING_OPERATION) {
                    string = false;
                }
            }
//          if we're not in a comment and not in a String but the current character is a String border
            else if (code.charAt(ind) == FalseLanguageDefinition.PRINT_STRING_OPERATION) {
//              we'll signalize that we're from now on in a String
                string = true;
            }
//          if we're not in a comment and not in a String and the current character is a comment-beginning
            else if (code.charAt(ind) == FalseLanguageDefinition.COMMENT_BEGIN) {
//              we'll signalize that we're from now on in a comment
                comment = true;
                comment_beg = ind;
            }
//          if we're not in a String and not in a comment but the previous character is a character
            else if(code.charAt(ind) == FalseLanguageDefinition.CHARACTER_DECLARATION){
                ind++;
            }
//          if we're not in a String and not in a comment but the current character is whitespace
            else if (Character.isWhitespace(code.charAt(ind))) {
//              and the whitespace is not required (i.e. there are not digits to the left AND to the right of the whitespace):
//              lets say we have the code "100 12" then 100 and 12 are two different operations whereas the whitespace inbetween is required
                if (!(ind > 0 && isNumerical(code.charAt(ind - 1))
                        && ind < code.length() && isNumerical(code.charAt(ind + 1)))) {
//                  we'll strip the whitespace and set the index to before the whitespace
//                  (for the same reason as after the comment)
                    code = code.substring(0, ind) + code.substring(ind + 1);
                    ind--;
                }
            }

            ind++;
        }

//      lastly we'll return the stripped code
        return code;
    }

    /**
     * This method evaluates a snippet from the given code that centrally displays the character at the index token.<br>
     * If there is any leading or trailing code that is not displayed it will be snipped with triple dots ("...") to show
     * that this is only a snippet of the code.
     *
     * @param token the index of the token you want to centrally being displayed
     * @param compilableCode The code whose token you want to display
     * @return the String that centrally displays the token'ths token from the String compilableCode
     */
    private static String evaluateCodeSnippet(int token, String compilableCode){
//      if there is any character that might be displayed
        if(compilableCode.length() > 0) {
//          if the token is the first in the code we'll have to begin at the index 0 otherwise we'll begin at the previous token
            int beg = (token - 1 < 0) ? 0 : token - 1;

//          if the first token to display is not also the first token in the code we'll begin with triple dots
            String codeSnippet = (beg == 0) ? "" : "... ";

//          we'll display maximum three tokens of the code
            for (int i = 0; i < 3 && (i + beg) < compilableCode.length(); i++) {
                codeSnippet += compilableCode.charAt(beg + i);

//              and if the last token we want to display is not the last token in the code
                if (i == 2 && (i + beg + 1) < compilableCode.length()) {
//                  we'll append a triple dot
                    codeSnippet += " ...";
                }
            }

//          finally we return the code snippet we evaluated
            return codeSnippet;
        }
//      if there is nothing to display we obviously won't display anything
        else{
            return "";
        }
    }

    /**
     * This mehtod determines whether a given char c is numerical in terms of the FALSE programming language
     *
     * @param c the character to be tested for being numerical
     * @return whether the character is numerical
     */
    private static boolean isNumerical(char c){
        return c >= '0' && c <= '9';
    }

    /**
     * This class may be used to properly count the indices of the tokens being parsed without losing the current index
     * by recursively parsing the code
     */
    public static class OccurrenceCounter {

        /**
         * The counter of the OccurrenceCounter-instance
         */
        private int ctr;

        /**
         * This constructor initializes a OccurrenceCounter-object with the value 0
         */
        public OccurrenceCounter(){
            ctr = 0;
        }

        /**
         * This method increments the index of this object and returns this value after.<br>
         * Thus the first value returned by this method will be {@code n + 1} with {@code n} being the number this objects
         * counter has been initialized with.<br>
         * After that every call will return {@code i + 1} with {@code i} being the result of the previous call of this method.<br>
         * Therefore any {@code j}th call of this method will return a value of {@code n + j}.
         *
         * @return The next index of this counter object
         */
        public int count(){
            return ++ctr;
        }
    }
}
