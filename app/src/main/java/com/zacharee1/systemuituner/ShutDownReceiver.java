package com.zacharee1.systemuituner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Zacha on 5/1/2017.
 */

public class ShutDownReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getText(R.string.sharedprefs_id).toString(), Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("safeStatbar", false)) {
            if (intent.getAction().equals(Intent.ACTION_SHUTDOWN) || intent.getAction().equals("android.intent.action.QUICKBOOT_POWEROFF") || intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
                Settings.Secure.putString(context.getContentResolver(), "icon_blacklist", "");
            }
        }
    }
}
