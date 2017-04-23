package com.zacharee1.systemuituner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {

    private Button rooted;
    private Button not_rooted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetThings setThings = new SetThings(this);

        setContentView(R.layout.activity_setup);

        TextView title = (TextView) findViewById(R.id.title_setup);
        title.setTextColor(setThings.titleText);

        rooted = (Button) findViewById(R.id.rooted);
        not_rooted = (Button) findViewById(R.id.not_rooted);

        buttons(rooted);
        buttons(not_rooted);
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
                }
            }
        });
    }
}
