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

package de.jeanpierrehotz.falseide.ui;

import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 */
public abstract class DialogCompat extends Dialog implements ActionListener {

    protected static final int TOP_MARGIN = 15;
    protected static final int OTHER_MARGIN = 3;
    protected static final int COMPONENT_MARGIN = 10;
    protected static final int MARGIN = 50;

    public DialogCompat(Frame frame) {
        super(frame);
    }

    public DialogCompat(Frame frame, boolean b) {
        super(frame, b);
    }

    public DialogCompat(Frame frame, String s) {
        super(frame, s);
    }

    public DialogCompat(Frame frame, String s, boolean b) {
        super(frame, s, b);
    }

    public DialogCompat(Frame frame, String s, boolean b, GraphicsConfiguration graphicsConfiguration) {
        super(frame, s, b, graphicsConfiguration);
    }

}
