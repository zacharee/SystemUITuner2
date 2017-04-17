package com.zacharee1.systemuituner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {

    Button rooted;
    Button not_rooted;
    Button to_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("com.zacharee1.sysuituner", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isDark", false)) {
            setTheme(R.style.DARK);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_setup);

        TextView title = (TextView) findViewById(R.id.title_setup);

        if (sharedPreferences.getBoolean("isDark", false)) {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        rooted = (Button) findViewById(R.id.rooted);
        not_rooted = (Button) findViewById(R.id.not_rooted);
        to_main = (Button) findViewById(R.id.open_main);

        buttons(rooted);
        buttons(not_rooted);
        buttons(to_main);
    }

    private void buttons(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button == rooted) {
                    Intent intent = new Intent(getApplicationContext(), RootActivity.class);
                    startActivity(intent);
                } else if (button == not_rooted) {
                    Intent intent = new Intent(getApplicationContext(), NoRoot.class);
                    startActivity(intent);
                } else if (button == to_main) {
                    SharedPreferences.Editor editor = getSharedPreferences("com.zacharee1.sysuituner", MODE_PRIVATE).edit();
                    editor.putBoolean("isSetup", true);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
