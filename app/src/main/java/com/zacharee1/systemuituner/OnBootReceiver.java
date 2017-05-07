package com.zacharee1.systemuituner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

/**
 * Created by Zacha on 5/1/2017.
 */

public class OnBootReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getText(R.string.sharedprefs_id).toString(), Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("safeStatbar", false)) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(Intent.ACTION_REBOOT) || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {
                String blacklist_bak = Settings.Secure.getString(context.getContentResolver(), "icon_blacklist2");
                Settings.Secure.putString(context.getContentResolver(), "icon_blacklist", blacklist_bak);
                Toast.makeText(context, context.getResources().getText(R.string.boot_message_icon_blacklist), Toast.LENGTH_SHORT).show();
                startWakefulService(context, new Intent(context, ShutDownListen.class));
            }
            if (intent.getData().toString().toLowerCase().contains("systemuituner")) {
                if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) || intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)) {
                    String blacklist_bak = Settings.Secure.getString(context.getContentResolver(), "icon_blacklist2");
                    Settings.Secure.putString(context.getContentResolver(), "icon_blacklist", blacklist_bak);
                    Toast.makeText(context, context.getResources().getText(R.string.boot_message_icon_blacklist), Toast.LENGTH_SHORT).show();
                    startWakefulService(context, new Intent(context, ShutDownListen.class));
                }
            }
        }
    }
}
