package com.habil.devhelper.utils;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;

/**
 * @author Habil BOZALi
 * @23 Kas 2015
 * @SaveUtil.java
 * @com.habil.devhelper
 */
public class SaveUtil {
    public static ISecurePreferences newNode() {
        ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
        return preferences.node("info");
    }
}
