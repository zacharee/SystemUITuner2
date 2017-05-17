package com.zacharee1.systemuituner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

public class WaitForBootActivity extends AppCompatActivity {
    private SetThings setThings;
    private BroadcastReceiver finish_activity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_boot);

        setThings = new SetThings(this);
        context = this;

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

        Button refresh = (Button) findViewById(R.id.refresh_boot);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setThings.sharedPreferences.getBoolean("isBooted", false)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(context, getResources().getText(R.string.not_booted_yet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(finish_activity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        super.onStop();
    }
}
