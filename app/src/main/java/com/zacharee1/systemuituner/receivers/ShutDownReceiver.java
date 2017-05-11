package com.zacharee1.systemuituner.receivers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 5/1/2017.
 */

public class ShutDownReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getText(R.string.sharedprefs_id).toString(), Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("safeStatbar", false)) {
            if (intent.getAction().equals(Intent.ACTION_SHUTDOWN) || intent.getAction().equals("android.intent.action.QUICKBOOT_POWEROFF")) {
                sharedPreferences.edit().putBoolean("isBooted", false).apply();
                try {
                    Settings.Secure.putString(context.getContentResolver(), "icon_blacklist", "");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, context.getResources().getText(R.string.permissions_failed), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
