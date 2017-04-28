package com.zacharee1.systemuituner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class SetupActivity extends AppCompatActivity {

    private Button rooted;
    private Button not_rooted;

    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetThings setThings = new SetThings(this);
        activity = this;

        setContentView(R.layout.activity_setup);

        TextView title = (TextView) findViewById(R.id.title_setup);
        title.setTextColor(setThings.titleText);

        rooted = (Button) findViewById(R.id.rooted);
        not_rooted = (Button) findViewById(R.id.not_rooted);

        //button listeners (need to be moved to setThings eventually)
        /* TODO: move button listeners */
        buttons(rooted);
        buttons(not_rooted);

        BroadcastReceiver broadcast_receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcast_receiver, new IntentFilter("finish_activity"));
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
