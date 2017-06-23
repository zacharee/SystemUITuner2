package com.zacharee1.systemuituner.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.zacharee1.systemuituner.R;
import com.zacharee1.systemuituner.SetThings;

public class NoRootSystemSettingsActivity extends AppCompatActivity {

    //private AppCompatActivity activity;
    private BroadcastReceiver finish_activity;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetThings setThings = new SetThings(this);
        setContentView(R.layout.activity_no_root_system_settings);
        //activity = this;

        String oneZero = setThings.sharedPreferences.getString("isSystemSwitchEnabled", "EXAMPLE_VALUE");
        String setting = setThings.sharedPreferences.getString("systemSettingKey", "EXAMPLE_SETTING");

        TextView title = findViewById(R.id.system_settings_title);
        title.setTextColor(setThings.titleText);

        TextView command = findViewById(R.id.adb_system_setting);
        command.setText("adb shell settings put system".concat(" ").concat(setting).concat(" " ).concat(oneZero)); //set TextView to System Settings value that was toggled

        Button perms = findViewById(R.id.set_sys_perm);
        Button go = findViewById(R.id.do_the_dirty);

        //button listeners
        setThings.buttons(perms, "SystemSettingsPerms");
        setThings.buttons(go, "WriteSystemSettings");

        finish_activity = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_systemuituner_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(finish_activity, new IntentFilter("finish_systemuituner_activity"));
    }

    @Override
    protected void onStop()
    {
        try {
            unregisterReceiver(finish_activity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        super.onStop();
    }
}
