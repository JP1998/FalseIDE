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

package de.jeanpierrehotz.falseide.ressources;

import de.jeanpierrehotz.falseide.ressources.languageindependence.Context;
import de.jeanpierrehotz.falseide.ressources.languageindependence.R;

import java.io.File;

/**
 * This class is usd for static Ressources for the False IDE.<br>
 * It loads the language independent ressources provided by the {@link Context}-class once and
 * then provides these values consistently without having to always call the method {@link Context#getString(int)}.<br>
 * Also it provides other ressources like preference keys, default values and more.
 */
public class Ressources {

    /**
     * Since this class is only used for storage of Ressources that are to be accessed
     * statically we don't want anyone to instantiate this class.
     */
    private Ressources(){}

    public static final String APPLICATION_PREFERENCE_NAME                                          = "FalseIDEMetaData";
    public static final String DEFAULT_ABSOLUTEWORKSPACE                                            = File.listRoots()[0] + "FalseIDE" + File.separator + "Projects" + File.separator;
    public static final String DEFAULT_RELATIVEWORKSPACE                                            = "FalseIDEProjects" + File.separator;
    public static final String DEFAULT_LASTPROJECT                                                  = "";
    public static final int DEFAULT_LOGGINGLEVEL                                                    = 2;
    public static final boolean DEFAULT_MOBILEUSE                                                   = false;
    public static final int DEFAULT_FILEEDITOR_FONTSIZE                                             = 12;
    public static final int DEFAULT_CONSOLEEDITOR_FONTSIZE                                          = 12;

    public static final String APPLICATION_NAME                                                     = Context.getString(R.string.application_name);

    public static final String SOURCE_FILE_ENDING                                                   = "_false.f";
    public static final String BYTE_FILE_ENDING                                                     = "_bytecode.out";

    public static final String BYTECODE_FILEEXTENSION_DESCRIPTION                                   = Context.getString(R.string.bytecode_fileextension_description);
    public static final String BYTECODE_FILEEXTENSION                                               = Context.getString(R.string.bytecode_fileextension);

    public static final String COMPILATION_ENDING_SUCCESS                                           = Context.getString(R.string.compilation_ending_success);
    public static final String COMPILATION_ENDING_FAILED                                            = Context.getString(R.string.compilation_ending_failed);

    public static final String PROGRAM_EXECUTION_START                                              = Context.getString(R.string.program_execution_start);
    public static final String PROGRAM_EXECUTION_END                                                = Context.getString(R.string.program_execution_end);
    public static final String PROGRAM_EXECUTION_ENDED_AFTER_ERROR                                  = Context.getString(R.string.program_execution_ended_after_error);

    public static final String ERROR_LOGGER_PREFIX                                                  = Context.getString(R.string.error_logger_prefix);
    public static final String ERROR_LOGGER_STACKTRACE_PREFIX                                       = Context.getString(R.string.error_logger_stacktrace_prefix);
    public static final String ERROR_LOGGER_STACKTRACE_MESSAGE_INTRO                                = Context.getString(R.string.error_logger_stacktrace_message_intro);
    public static final String ERROR_LOGGER_STACKTRACE_MESSAGE_PREFIX                               = Context.getString(R.string.error_logger_stacktrace_message_prefix);

    public static final String ERROR_SAVEPROJECT_IOEXCEPTION                                        = Context.getString(R.string.error_saveproject_ioexception);
    public static final String ERROR_CREATEPROJECT_IOEXCEPTION                                      = Context.getString(R.string.error_createproject_ioexception);
    public static final String ERROR_COMPILEPROJECT_COMPILEERROR                                    = Context.getString(R.string.error_compileproject_compileerror);
    public static final String ERROR_COMPILEPROJECT_IOEXCEPTION                                     = Context.getString(R.string.error_compileproject_ioexception);
    public static final String ERROR_EXECUTEPROJECT_INTERPRETERROR                                  = Context.getString(R.string.error_executeproject_interpreterror);
    public static final String ERROR_EXECUTEPROJECT_OTHEREXCEPTION                                  = Context.getString(R.string.error_executeproject_otherexception);
    public static final String ERROR_GETFILECONTENT_IOEXCEPTION                                     = Context.getString(R.string.error_getfilecontent_ioexception);
    public static final String ERROR_SHOWLOG_IOEXCEPTION                                            = Context.getString(R.string.error_showlog_ioexception);
    public static final String ERROR_EXECUTEPROJECTWITHOUTCOMPILING_IOEXCEPTION                     = Context.getString(R.string.error_executeprojectwithoutcompiling_ioexception);
    public static final String ERROR_DECOMPILEPROJECT_IOEXCEPTION                                   = Context.getString(R.string.error_decompileproject_ioexception);

    public static final String DECOMPILEPROJECT_ERRORMESSAGE                                        = Context.getString(R.string.decompileproject_errormessage);

    public static final String MENUBAR_FILE_CAPTION                                                 = Context.getString(R.string.menubar_file_caption);
    public static final String MENUBAR_FILE_CREATEPROJECT_CAPTION                                   = Context.getString(R.string.menubar_file_createproject_caption);
    public static final String MENUBAR_FILE_SAVEPROJECT_CAPTION                                     = Context.getString(R.string.menubar_file_saveproject_caption);
    public static final String MENUBAR_FILE_SETTINGS_CAPTION                                        = Context.getString(R.string.menubar_file_settings_caption);
    public static final String MENUBAR_FILE_EXITAPPLICATION_CAPTION                                 = Context.getString(R.string.menubar_file_exitapplication_caption);

    public static final String MENUBAR_CODE_CAPTION                                                 = Context.getString(R.string.menubar_code_caption);
    public static final String MENUBAR_CODE_COMPILE_CAPTION                                         = Context.getString(R.string.menubar_code_compile_caption);
    public static final String MENUBAR_CODE_EXECUTE_CAPTION                                         = Context.getString(R.string.menubar_code_execution_caption);
    public static final String MENUBAR_CODE_EXECUTEWITHOUTCOMPILING_CAPTION                         = Context.getString(R.string.menubar_code_executewithoutcompiling_caption);
    public static final String MENUBAR_CODE_DECOMPILE_CAPTION                                       = Context.getString(R.string.menubar_code_decompile_caption);

    public static final String MENUBAR_CODE_ICON_COMPILE                                            = Context.getString(R.string.menubar_code_icon_compile);
    public static final String MENUBAR_CODE_ICON_EXECUTE                                            = Context.getString(R.string.menubar_code_icon_execute);
    public static final String MENUBAR_CODE_ICON_DECOMPILE                                          = Context.getString(R.string.menubar_code_icon_decompile);
    public static final String MENUBAR_CODE_ICON_EXECUTEWITHOUTCOMPILATION                          = Context.getString(R.string.menubar_code_icon_executewithoutcompilation);

    public static final String MENUBAR_ERRORLOG_CAPTION                                             = Context.getString(R.string.menubar_errorlog_caption);
    public static final String MENUBAR_ERRORLOG_SHOW_CAPTION                                        = Context.getString(R.string.menubar_errorlog_show_caption);
    public static final String MENUBAR_ERRORLOG_SAVE_CAPTION                                        = Context.getString(R.string.menubar_errorlog_save_caption);
    public static final String MENUBAR_ERRORLOG_CLEAR_CAPTION                                       = Context.getString(R.string.menubar_errorlog_clear_caption);

    public static final String CONTEXTMENU_NOPROJECTSELECTED_CREATENEWPROJECT                       = Context.getString(R.string.contextmenu_noprojectselected_createnewproject);
    public static final String CONTEXTMENU_NOPROJECTSELECTED_REFRESHWORKSPACE                       = Context.getString(R.string.contextmenu_noprojectselected_refreshworkspace);

    public static final String CONTEXTMENU_PROJECTSELECTED_OPENPROJECT                              = Context.getString(R.string.contextmenu_projectselected_openproject);
    public static final String CONTEXTMENU_PROJECTSELECTED_COMPILEPROJECT                           = Context.getString(R.string.contextmenu_projectselected_compileproject);
    public static final String CONTEXTMENU_PROJECTSELECTED_EXECUTEPROJECT                           = Context.getString(R.string.contextmenu_projectselected_executeproject);
    public static final String CONTEXTMENU_PROJECTSELECTED_DELETEPROJECT                            = Context.getString(R.string.contextmenu_projectselected_deleteproject);
    public static final String CONTEXTMENU_PROJECTSELECTED_EXECUTEPROJECTWITHOUTCOMPILING           = Context.getString(R.string.contextmenu_projectselected_executeprojectwithoutcompiling);

    public static final String DIALOG_CLOSEACTION                                                   = Context.getString(R.string.dialog_closeaction);
    public static final String DIALOG_OK                                                            = Context.getString(R.string.dialog_ok);
    public static final String DIALOG_ABORT                                                         = Context.getString(R.string.dialog_abort);

    public static final String CREATEPROJECTDIALOG_CAPTION                                          = Context.getString(R.string.createprojectdialog_caption);
    public static final String CREATEPROJECTDIALOG_NAME_CAPTION                                     = Context.getString(R.string.createprojectdialog_name_caption);

    public static final String DELETEPROJECTDIALOG_CAPTION                                          = Context.getString(R.string.deleteprojectdialog_caption);
    public static final String DELETEPROJECTDIALOG_MESSAGE_SUCCESS                                  = Context.getString(R.string.deleteprojectdialog_message_success);
    public static final String DELETEPROJECTDIALOG_MESSAGE_FAIL                                     = Context.getString(R.string.deleteprojectdialog_message_fail);

    public static final String IOERRORDIALOG_CAPTION                                                = Context.getString(R.string.ioerrordialog_caption);
    public static final String IOERRORDIALOG_MESSAGE                                                = Context.getString(R.string.ioerrordialog_message);

    public static final String PROGRAMALREADYRUNNINGDIALOG_CAPTION                                  = Context.getString(R.string.programalreadyrunningdialog_caption);
    public static final String PROGRAMALREADYRUNNINGDIALOG_MESSAGE                                  = Context.getString(R.string.programalreadyrunningdialog_message);

    public static final String UNSAVEDPROGRESSDIALOG_CAPTION                                        = Context.getString(R.string.unsavedprogressdialog_caption);
    public static final String UNSAVEDPROGRESSDIALOG_MESSAGE                                        = Context.getString(R.string.unsavedprogressdialog_message);

    public static final String ERRORLOGNOTOPENABLEDIALOG_CAPTION                                    = Context.getString(R.string.errorlognotopenabledialog_caption);
    public static final String ERRORLOGNOTOPENABLEDIALOG_MESSAGE                                    = Context.getString(R.string.errorlognotopenabledialog_message);

    public static final String CHANGESETTINGSDIALOG_CAPTION                                         = Context.getString(R.string.changesettingsdialog_caption);
    public static final String CHANGESETTINGSDIALOG_MOBILEUSECHECKBOX_CAPTION                       = Context.getString(R.string.changesettingsdialog_mobileusecheckbox_caption);
    public static final String CHANGESETTINGSDIALOG_RELATIVEWORKSPACELABEL_CAPTION                  = Context.getString(R.string.changesettingsdialog_relativeworkspacelabel_caption);
    public static final String CHANGESETTINGSDIALOG_ABSOLUTEWORKSPACELABEL_CAPTION                  = Context.getString(R.string.changesettingsdialog_absoluteworkspacelabel_caption);
    public static final String CHANGESETTINGSDIALOG_SELECTABSOLUTEFOLDERBUTTON_CAPTION              = Context.getString(R.string.changesettingsdialog_selectabsolutefolderbutton_caption);
    public static final String CHANGESETTINGSDIALOG_SELECTRELATIVEFOLDERBUTTON_CAPTION              = Context.getString(R.string.changesettingsdialog_selectabsolutefolderbutton_caption);
    public static final String CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_CAPTION                       = Context.getString(R.string.changesettingsdialog_logginglevellabel_caption);
    public static final String CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_MSG1                          = Context.getString(R.string.changesettingsdialog_logginglevellabel_msg1);
    public static final String CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_MSG2                          = Context.getString(R.string.changesettingsdialog_logginglevellabel_msg2);
    public static final String CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_MSG3                          = Context.getString(R.string.changesettingsdialog_logginglevellabel_msg3);
    public static final String CHANGESETTINGSDIALOG_LOGGINGLEVELLABEL_MSG4                          = Context.getString(R.string.changesettingsdialog_logginglevellabel_msg4);

    public static final String NOTCOMPILEDEXECUTIONERRORDIALOG_CAPTION                              = Context.getString(R.string.notcompiledexecutionerrordialog_caption);
    public static final String NOTCOMPILEDEXECUTIONERRORDIALOG_MESSAGE                              = Context.getString(R.string.notcompiledexecutionerrordialog_message);

    public static final String DECOMPILEDIALOG_CAPTION                                              = Context.getString(R.string.decompiledialog_caption);
    public static final String DECOMPILEDIALOG_PATHLABEL                                            = Context.getString(R.string.decompiledialog_pathlabel);
    public static final String DECOMPILEDIALOG_SELECTFILEBUTTON                                     = Context.getString(R.string.decompiledialog_selectfilebutton);

    public static final String ABORTBUTTON_TOOLTIP                                                  = Context.getString(R.string.abortbutton_tooltip);
    public static final String CONSOLE_LABEL                                                        = Context.getString(R.string.console_label);

    public static final String PROJECTTREE_LABEL                                                    = Context.getString(R.string.projecttree_label);

    public static final class PreferenceKeys {

        private PreferenceKeys(){}

        public static final String PREFERENCE_ABSOLUTEWORKSPACE_KEY         = "FalseIDEMetaData_AbsoluteWorkspace_0x00000001";
        public static final String PREFERENCE_LASTPROJECT_KEY               = "FalseIDEMetaData_LastProject_0x00000002";
        public static final String PREFERENCE_LOGGINGLEVEL_KEY              = "FalseIDEMetaData_LoggingLevel_0x00000003";
        public static final String PREFERENCE_RELATIVEWORKSPACE_KEY         = "FalseIDEMetaData_RelativeWorkspace_0x00000004";
        public static final String PREFERENCE_MOBILEUSE_KEY                 = "FalseIDEMetaData_MobileUse_0x00000005";
        public static final String PREFERENCE_FILEEDITOR_FONTSIZE_KEY       = "FalseIDEMetaData_FileEditorFontSize_0x00000006";
        public static final String PREFERENCE_CONSOLEEDITOR_FONTSIZE_KEY    = "FalseIDEMetaData_ConsoleEditorFontSize_0x00000007";
    }

    public static final class LoggingLevels{

        private LoggingLevels(){}

        public static final int EVERY       = 1;
        public static final int MODERATE    = 2;
        public static final int FATAL       = 3;
        public static final int NONE        = 4;
    }

}
