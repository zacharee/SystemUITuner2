package com.zacharee1.systemuituner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Zacha on 4/19/2017.
 */

public class SetThings {
    public boolean Dark;
    public boolean setup;

    public int layoutBackground;
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

        layoutBackground = Dark ? R.drawable.layout_bg_dark : R.drawable.layout_bg_light;
        titleText = activity.getResources().getColor(Dark ? android.R.color.primary_text_dark : android.R.color.primary_text_light);
        drawerItem = Dark ? activity.getResources().getColorStateList(R.color.drawer_item_dark) : activity.getResources().getColorStateList(R.color.drawer_item_light);

        activity.setTheme(SetupActivity.class == activity.getClass() ? Dark ? R.style.DARK : R.style.AppTheme : Dark ? R.style.DARK_NoAppBar : R.style.AppTheme_NoActionBar);

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

        context = currentActivity.getApplicationContext();
    }

    public void buttons(final Button button, final String name) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (name) {
                    case "setup":
                        intent = new Intent(currentActivity.getApplicationContext(), SetupActivity.class);
                        currentActivity.startActivity(intent);
                        break;
                    case "enableDemo":
                        settings("global", "sysui_demo_allowed", 1);
                        break;
                    case "setupDone":
                        editor.putBoolean("isSetup", true);
                        editor.commit();
                        sudo("pm grant com.zacharee1.systemuituner android.permission.DUMP ; pm grant com.zacharee1.systemuituner android.permission.WRITE_SECURE_SETTINGS");
                        intent = new Intent(currentActivity.getApplicationContext(), MainActivity.class);
                        currentActivity.startActivity(intent);
                        break;
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
                    Runtime.getRuntime().exec("content insert --uri content://settings/system --bind name:s:" + pref + " --bind value:i:" + value);
                    break;
            }
        } catch (Exception e) {
            Exceptions exceptions = new Exceptions();
            exceptions.secureSettings(context, currentActivity.getApplicationContext(), e.getMessage(), "Uh oh");
        }
    }

    public void sudo(String...strings) {
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
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(currentActivity.getApplicationContext(), "Great! Go play around!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("No Root?", e.getMessage());
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(currentActivity.getApplicationContext(), "You sure you're rooted?", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            outputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
