package com.zacharee1.systemuituner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Zacha on 4/19/2017.
 */

public class SetThings {
    public boolean Dark;
    public boolean setup;

    public int titleText;
    public int style;
    public ColorStateList drawerItem;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public ArrayList<Integer> pages;

    public Activity currentActivity;

    public Context context;

    public SetThings(Activity activity) {
        sharedPreferences = activity.getSharedPreferences("com.zacharee1.sysuituner", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Dark = sharedPreferences.getBoolean("isDark", false);
        setup = sharedPreferences.getBoolean("isSetup", false);

        titleText = activity.getResources().getColor(Dark ? android.R.color.primary_text_dark : android.R.color.primary_text_light);
        drawerItem = Dark ? activity.getResources().getColorStateList(R.color.drawer_item_dark) : activity.getResources().getColorStateList(R.color.drawer_item_light);

        activity.setTheme(SetupActivity.class == activity.getClass() || NoRootSystemSettingsActivity.class == activity.getClass() ? Dark ? R.style.DARK : R.style.AppTheme : Dark ? R.style.DARK_NoAppBar : R.style.AppTheme_NoActionBar);

        style = Dark ? R.style.DARK_NoAppBar : R.style.AppTheme_NoActionBar;

        pages = new ArrayList<Integer>() {{
                add(R.id.nav_home);
                add(R.id.nav_statusbar);
                add(R.id.nav_demo_mode);
                add(R.id.nav_about);
                add(R.id.nav_settings);
                add(R.id.nav_misc);
            }};

        currentActivity = activity;

        context = currentActivity;
    }

    public void buttons(final Button button, final String name) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                try {
                    switch (name) {
                        case "setup":
                            intent = new Intent(currentActivity.getApplicationContext(), SetupActivity.class);
                            currentActivity.startActivity(intent);
                            break;
                        case "enableDemo":
                            settings("global", "sysui_demo_allowed", 1);
                            break;
                        case "setupDoneRoot":
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    sudo("pm grant com.zacharee1.systemuituner android.permission.DUMP ; pm grant com.zacharee1.systemuituner android.permission.WRITE_SECURE_SETTINGS");
                                }
                            }).start();
                        case "setupDone":
                            editor.putBoolean("isRooted", name.equals("setupDoneRoot"));
                            editor.putBoolean("isSetup", true);
                            editor.apply();
                            intent = new Intent(currentActivity.getApplicationContext(), MainActivity.class);
                            currentActivity.startActivity(intent);
                            break;
                        case "SystemSettingsPerms":
                            intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + currentActivity.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            currentActivity.startActivity(intent);
                            break;
                        case "WriteSystemSettings":
                            Settings.System.putInt(currentActivity.getContentResolver(), sharedPreferences.getString("systemSettingKey", ""), Integer.decode(sharedPreferences.getString("isSystemSwitchEnabled", "0")));
                            break;
                    }
                } catch (Exception e) {
                    Exceptions exceptions = new Exceptions();
                    exceptions.systemSettings(context, currentActivity.getApplicationContext(), e.getMessage(), "SetThings");
                }
            }
        });
    }

    public void settings(final String type, final String pref, final int value) {
        try {
            switch (type) {
                case "global":
                    Settings.Global.putInt(currentActivity.getContentResolver(), pref, value);
                    break;
                case "secure":
                    Settings.Secure.putInt(currentActivity.getContentResolver(), pref, value);
                    break;
                case "system":
                    if (sharedPreferences.getBoolean("isRooted", true)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sudo("settings put system " + pref + " " + value);
                            }
                        }).start();
                    }
                    else {
                        editor.putString("isSystemSwitchEnabled", String.valueOf(value));
                        editor.putString("systemSettingKey", pref);
                        editor.apply();
                        Intent intent = new Intent(currentActivity.getApplicationContext(), NoRootSystemSettingsActivity.class);
                        currentActivity.startActivity(intent);
                    }
            }
        } catch (Exception e) {
            Exceptions exceptions = new Exceptions();
            exceptions.secureSettings(context, currentActivity.getApplicationContext(), e.getMessage(), "SetThings");
        }
    }

    private void sudo(String...strings) {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("No Root?", e.getMessage());
            }
            outputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
