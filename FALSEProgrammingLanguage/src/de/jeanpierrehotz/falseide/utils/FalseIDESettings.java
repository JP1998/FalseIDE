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

package de.jeanpierrehotz.falseide.utils;

import de.jeanpierrehotz.falseide.ressources.Ressources;

import java.io.File;

/**
 * This class resembles all the settings that are changeable by the user in the FalseIDE.<br>
 * It is known to be used in the classes {@link de.jeanpierrehotz.falseide.FalseIDE} and the
 * {@link de.jeanpierrehotz.falseide.ui.settingsui.ChangeSettingsDialog}<br>
 * Any constraints on values of the settings are enforced by the set-methods.
 */
public class FalseIDESettings {

    /**
     * The logging levels range from 1 - 4:
     * <ol>
     * <li>- log everything there is to log</li>
     * <li>- log anything that might be of interest when there is a failure in the IDE (anything expected but uncommon)</li>
     * <li>- only log fatal errors (anything unexpected)</li>
     * <li>- don't you dare log anything!</li>
     * </ol>
     */
    private static final int MIN_LOGLEVEL = Ressources.LoggingLevels.EVERY,
            MAX_LOGLEVEL = Ressources.LoggingLevels.NONE;

    /**
     * The absolute path to the workspace used in this instance of this program.
     * For further information about the workspace see {@link #getWorkSpace()}
     */
    private String absoluteWorkSpace;

    /**
     * The relative path to the workspace used in this instance of this program.
     * For further information about the workspace see {@link #getWorkSpace()}
     */
    private String relativeWorkSpace;

    /**
     * Whether the IDE is used in mobile mode or not.
     * This determines whether the relative or absolute path to the workspace is used.
     */
    private boolean mobileUse;

    /**
     * The logging level that shows what kind of errors are supposed to be logged.<br>
     * The Logging level may contain values ranging from {@link #MIN_LOGLEVEL} to {@link #MAX_LOGLEVEL}
     */
    private int loggingLevel;

    /**
     * This constructor loads the settings for the given Preference
     *
     * @param applicationPrefs the applications preference which the settings are loaded from
     */
    public FalseIDESettings(Preference applicationPrefs) {
        this.absoluteWorkSpace = applicationPrefs.getString(Ressources.PreferenceKeys.PREFERENCE_ABSOLUTEWORKSPACE_KEY, Ressources.DEFAULT_ABSOLUTEWORKSPACE);
        this.relativeWorkSpace = applicationPrefs.getString(Ressources.PreferenceKeys.PREFERENCE_RELATIVEWORKSPACE_KEY, Ressources.DEFAULT_RELATIVEWORKSPACE);
        this.mobileUse = applicationPrefs.getBoolean(Ressources.PreferenceKeys.PREFERENCE_MOBILEUSE_KEY, Ressources.DEFAULT_MOBILEUSE);
        this.loggingLevel = applicationPrefs.getInt(Ressources.PreferenceKeys.PREFERENCE_LOGGINGLEVEL_KEY, Ressources.DEFAULT_LOGGINGLEVEL);
    }

    /**
     * This constructor copies the given settings into a new object
     *
     * @param copy the settings that are to be copied
     */
    public FalseIDESettings(FalseIDESettings copy) {
        this.absoluteWorkSpace = copy.absoluteWorkSpace;
        this.relativeWorkSpace = copy.relativeWorkSpace;
        this.mobileUse = copy.mobileUse;
        this.loggingLevel = copy.loggingLevel;
    }

    /**
     * This method gives you the path to the workspace that is to be used.<br>
     * The workspace may contain any number of projects where their tree will look like this:
     * <pre><code>
     * &lt;Workspace-path&gt;
     *  |- &lt;Project1-Name&gt;
     *      |- &lt;Project1-Name&gt;_false.f
     *      |- [&lt;Project1-Name&gt;_bytecode.out]
     *  |- &lt;Project2-Name&gt;
     *      |- &lt;Project2-Name&gt;_false.f
     *      |- [&lt;Project2-Name&gt;_bytecode.out]
     * </code></pre>
     * In this case the <pre><code>&lt;ProjectX-Name&gt;</code></pre> is the folder used for a project,
     * <pre><code>&lt;ProjectX-Name&gt;_false.f</code></pre> is the file containing the source code to the project
     * and <pre><code>&lt;ProjectX-Name&gt;_bytecode.out</code></pre> is the byte code generated by the
     * {@link de.jeanpierrehotz.falseprogramminglanguage.compiler.FalseCompiler} after compiling
     * the source code. This file does not have to exist and might not be up to date.<br>
     * The workspaces path is required to have the String {@link File#separator} at its end.
     *
     * @return the path to the workspace that is currently in use
     */
    public String getWorkSpace() {
//      if we are not in mobile mode
        if (!mobileUse) {
//          we can simply return the absolute path
            return absoluteWorkSpace;
        }
//      otherwise, if we're in mobile mode
        else {
//          we'll need to create an absolute path from the relative path
            String relativeWS = new File(relativeWorkSpace).getAbsolutePath();

//          make sure this path is ended by an File.separator
            if (!relativeWS.endsWith(File.separator)) {
                relativeWS += File.separator;
            }

//          and then return this path
            return relativeWS;
        }
    }

    /**
     * This method gives you the absolute workspace saved by this object.<br>
     * This does not have to denote the same file or directory as the relative workspace!
     *
     * @return the absolute workspace
     */
    public String getAbsoluteWorkSpace() {
        return absoluteWorkSpace;
    }

    /**
     * This method gives you the relative workspace saved by this object.<br>
     * This does not have to denote the same file or directory as the absolute workspace!
     *
     * @return the relative workspace
     */
    public String getRelativeWorkSpace() {
        return relativeWorkSpace;
    }

    /**
     * This method shows you whether or not the IDE is currently in mobile use
     *
     * @return whether we are in mobile use or not
     */
    public boolean isMobileUse() {
        return mobileUse;
    }

    /**
     * This method gives you the logging level this IDE is currently using.<br>
     * To get this integers meaning take a look at {@link de.jeanpierrehotz.falseide.ressources.Ressources.LoggingLevels}
     *
     * @return the logging level
     */
    public int getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * This method sets the absolute workspace to the given path and makes sure it has {@link File#separator} at its end
     *
     * @param newWorkSpace the absolute workspace path that should be used from now on
     */
    public void setAbsoluteWorkSpace(String newWorkSpace) {
//      enforce the File.separator constraint
        if (!newWorkSpace.endsWith(File.separator)) {
            newWorkSpace += File.separator;
        }

//      and save the new workspace
        this.absoluteWorkSpace = newWorkSpace;
    }

    /**
     * This method sets the absolute workspace to the given path and makes sure it has {@link File#separator} at its end
     *
     * @param relativeWorkSpace the absolute workspace path that should be used from now on
     */
    public void setRelativeWorkSpace(String relativeWorkSpace) {
//      enforce the File.separator constraint
        if (!relativeWorkSpace.endsWith(File.separator)) {
            relativeWorkSpace += File.separator;
        }

//      and save the new workspace
        this.relativeWorkSpace = relativeWorkSpace;
    }

    /**
     * This method sets whether this IDE is to be used in mobile mode or not
     *
     * @param mu whether the IDE should be in mobile mode or not
     */
    public void setMobileUse(boolean mu) {
        this.mobileUse = mu;
    }

    /**
     * This method sets the logging level that is to be used
     *
     * @param loggingLevel the new logging level
     */
    public void setLoggingLevel(int loggingLevel) {
//      if the new logging level is in range
        if (loggingLevel >= MIN_LOGLEVEL && loggingLevel <= MAX_LOGLEVEL) {
//          we'll save it
            this.loggingLevel = loggingLevel;
        }
    }

    /**
     * This mehtod determines whether an error with given logging level is to be logged or not
     *
     * @param errorsLogLevel the logging level of the error for which we shall determine whether to log it or not
     * @return whether you should log the error with given logging level or not
     */
    public boolean isToBeLogged(int errorsLogLevel) {
        return errorsLogLevel >= loggingLevel;
    }

    /**
     * This method saves the settings to given {@link de.jeanpierrehotz.falseide.utils.Preference.Editor}-object
     * and returns this object thereafter again.
     *
     * @param edit the preferences editor to save to
     * @return the preferences editor for easy commiting or applying
     */
    public Preference.Editor saveToPreferenceEditor(Preference.Editor edit) {
        return edit
                .putString(Ressources.PreferenceKeys.PREFERENCE_ABSOLUTEWORKSPACE_KEY, absoluteWorkSpace)
                .putString(Ressources.PreferenceKeys.PREFERENCE_RELATIVEWORKSPACE_KEY, relativeWorkSpace)
                .putBoolean(Ressources.PreferenceKeys.PREFERENCE_MOBILEUSE_KEY, mobileUse)
                .putInt(Ressources.PreferenceKeys.PREFERENCE_LOGGINGLEVEL_KEY, loggingLevel);
    }

}
