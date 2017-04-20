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

    SetThings setThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);

        setContentView(R.layout.activity_setup);

        TextView title = (TextView) findViewById(R.id.title_setup);
        title.setTextColor(setThings.titleText);

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
                    Intent intent = new Intent(getApplicationContext(), NoRootActivity.class);
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
