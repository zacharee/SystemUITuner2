package com.zacharee1.systemuituner;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Zacha on 4/15/2017.
 */

@SuppressWarnings("ALL")
public class Utils {

    // TODO: move this to setThings
    private final static int THEME_DEFAULT = 0;
    private final static int THEME_DARK = 1;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
}
