package com.habil.devhelper.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * @author Habil BOZALi
 * @15 Eki 2015
 * @ColorUtil.java
 * @com.habil.devhelper
 */
public class ColorUtil {

    public static Color getColor(int color) {
        Display display = Display.getCurrent();
        return display.getSystemColor(color);
    }

    public static Color getColorByRGB(int r, int g, int b) {
        Display display = Display.getCurrent();
        return new Color(display, r, g, b);
    }
}
