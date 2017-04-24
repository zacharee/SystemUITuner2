package com.zacharee1.systemuituner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class NoRootSystemSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetThings setThings = new SetThings(this);
        setContentView(R.layout.activity_no_root_system_settings);
        setTitle("System Settings");

        String oneZero = setThings.sharedPreferences.getString("isSystemSwitchEnabled", "EXAMPLE_VALUE");
        String setting = setThings.sharedPreferences.getString("systemSettingKey", "EXAMPLE_SETTING");

        TextView title = (TextView) findViewById(R.id.system_settings_title);
        title.setTextColor(setThings.titleText);

        TextView command = (TextView) findViewById(R.id.adb_system_setting);
        command.setText("adb shell settings put system " + " " + setting + " " + oneZero); //set TextView to System Settings value that was toggled

        Button perms = (Button) findViewById(R.id.set_sys_perm);
        Button go = (Button) findViewById(R.id.do_the_dirty);

        //button listeners
        setThings.buttons(perms, "SystemSettingsPerms");
        setThings.buttons(go, "WriteSystemSettings");
    }
}
