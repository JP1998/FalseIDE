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

/**
 * Created by the program Java Ressource Generator built by Jean-Pierre Hotz
 * This class is (together with the class Context) created to prevent hardcoding and stuff like that
 * This class provides ids to values accessible via the static methods in the Context-class.
 * The values are stored in separate package-visible classes for each datatype.
 */
public class R{
    /**
     * This class contains all the publicly readable ids for strings.
     * Their language is handled in the method {@link Context#getString(int)}
     * You may get a string by calling following:
     * <pre><code>
     * String example = Context.getString(R.string.exampleid);
     * </code></pre>
     */
    public static class string{
        public static final int application_name = 0;
        public static final int compilation_ending_success = 1;
        public static final int compilation_ending_failed = 2;
        public static final int program_execution_start = 3;
        public static final int program_execution_end = 4;
        public static final int menubar_file_caption = 5;
        public static final int menubar_file_createproject_caption = 6;
        public static final int menubar_file_saveproject_caption = 7;
        public static final int menubar_file_exitapplication_caption = 8;
        public static final int menubar_code_caption = 9;
        public static final int menubar_code_compile_caption = 10;
        public static final int menubar_code_execution_caption = 11;
        public static final int menubar_code_icon_compile = 12;
        public static final int menubar_code_icon_execute = 13;
        public static final int contextmenu_noprojectselected_createnewproject = 14;
        public static final int contextmenu_noprojectselected_refreshworkspace = 15;
        public static final int contextmenu_projectselected_openproject = 16;
        public static final int contextmenu_projectselected_compileproject = 17;
        public static final int contextmenu_projectselected_executeproject = 18;
        public static final int contextmenu_projectselected_deleteproject = 19;
        public static final int dialog_closeaction = 20;
        public static final int dialog_ok = 21;
        public static final int dialog_abort = 22;
        public static final int createprojectdialog_caption = 23;
        public static final int createprojectdialog_name_caption = 24;
        public static final int deleteprojectdialog_caption = 25;
        public static final int deleteprojectdialog_message_success = 26;
        public static final int deleteprojectdialog_message_fail = 27;
        public static final int ioerrordialog_caption = 28;
        public static final int ioerrordialog_message = 29;
        public static final int programalreadyrunningdialog_caption = 30;
        public static final int programalreadyrunningdialog_message = 31;
        public static final int unsavedprogressdialog_caption = 32;
        public static final int unsavedprogressdialog_message = 33;
        public static final int abortbutton_tooltip = 34;
        public static final int console_label = 35;
        public static final int projecttree_label = 36;
        public static final int program_execution_ended_after_error = 37;
        public static final int error_logger_prefix = 38;
        public static final int error_logger_stacktrace_prefix = 39;
        public static final int error_logger_stacktrace_message_intro = 40;
        public static final int error_logger_stacktrace_message_prefix = 41;
        public static final int error_saveproject_ioexception = 42;
        public static final int error_createproject_ioexception = 43;
        public static final int error_compileproject_compileerror = 44;
        public static final int error_compileproject_ioexception = 45;
        public static final int error_executeproject_interpreterror = 46;
        public static final int error_executeproject_otherexception = 47;
        public static final int error_getfilecontent_ioexception = 48;
        public static final int error_showlog_ioexception = 49;
        public static final int menubar_file_settings_caption = 50;
        public static final int menubar_errorlog_caption = 51;
        public static final int menubar_errorlog_show_caption = 52;
        public static final int menubar_errorlog_save_caption = 53;
        public static final int menubar_errorlog_clear_caption = 54;
        public static final int errorlognotopenabledialog_caption = 55;
        public static final int errorlognotopenabledialog_message = 56;
        public static final int changesettingsdialog_caption = 57;
        public static final int changesettingsdialog_mobileusecheckbox_caption = 58;
        public static final int changesettingsdialog_relativeworkspacelabel_caption = 59;
        public static final int changesettingsdialog_absoluteworkspacelabel_caption = 60;
        public static final int changesettingsdialog_selectabsolutefolderbutton_caption = 61;
        public static final int changesettingsdialog_logginglevellabel_caption = 62;
        public static final int changesettingsdialog_logginglevellabel_msg1 = 63;
        public static final int changesettingsdialog_logginglevellabel_msg2 = 64;
        public static final int changesettingsdialog_logginglevellabel_msg3 = 65;
        public static final int changesettingsdialog_logginglevellabel_msg4 = 66;
        public static final int error_executeprojectwithoutcompiling_ioexception = 67;
        public static final int menubar_code_executewithoutcompiling_caption = 68;
        public static final int contextmenu_projectselected_executeprojectwithoutcompiling = 69;
        public static final int notcompiledexecutionerrordialog_caption = 70;
        public static final int notcompiledexecutionerrordialog_message = 71;
        public static final int bytecode_fileextension_description = 72;
        public static final int bytecode_fileextension = 73;
        public static final int error_decompileproject_ioexception = 74;
        public static final int decompileproject_errormessage = 75;
        public static final int menubar_code_decompile_caption = 76;
        public static final int menubar_code_icon_decompile = 77;
        public static final int menubar_code_icon_executewithoutcompilation = 78;
        public static final int decompiledialog_caption = 79;
        public static final int decompiledialog_pathlabel = 80;
        public static final int decompiledialog_selectfilebutton = 81;
    }

    /**
     * This class contains all the publicly readable ids for integer-values.
     * You may get a integer by calling following:
     * <pre><code>
     * int example = Context.getInt(R.integer.exampleid);
     * </code></pre>
     */
    public static class integer{
    }

    /**
     * This class contains all the publicly readable ids for color-values.
     * You may get a color by calling following:
     * <pre><code>
     * Color example = Context.getColor(R.color.exampleid);
     * </code></pre>
     */
    public static class color{
    }

}
