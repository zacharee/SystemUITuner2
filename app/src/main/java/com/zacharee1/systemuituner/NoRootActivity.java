package com.zacharee1.systemuituner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class NoRootActivity extends AppCompatActivity {

    // --Commented out by Inspection (5/7/2017 8:00 AM):private AppCompatActivity activity;
    private BroadcastReceiver finish_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetThings setThings = new SetThings(this);

        setContentView(R.layout.activity_no_root);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.noroot_title);

        title.setTextColor(setThings.titleText); //make sure title text color is right

        Button setupDone = (Button) findViewById(R.id.no_root_done);

        setThings.buttons(setupDone, "setupDone"); //button listener

        finish_activity = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(finish_activity, new IntentFilter("finish_activity"));
    }

    @Override
    protected void onStop()
    {
        try
        {
            unregisterReceiver(finish_activity);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        super.onStop();
    }

}
