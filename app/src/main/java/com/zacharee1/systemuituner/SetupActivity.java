package com.zacharee1.systemuituner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class SetupActivity extends AppCompatActivity {

    private Button rooted;
    private Button not_rooted;

    private AppCompatActivity activity;

    private SetThings setThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetThings setThings = new SetThings(this);
        activity = this;

        setThings = new SetThings(this);

        setContentView(R.layout.activity_setup);

        TextView title = (TextView) findViewById(R.id.title_setup);
        title.setTextColor(setThings.titleText);

        rooted = (Button) findViewById(R.id.rooted);
        not_rooted = (Button) findViewById(R.id.not_rooted);

        //button listeners
        setThings.buttons(rooted, "root_setup");
        setThings.buttons(not_rooted, "no_root_setup");

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
}
