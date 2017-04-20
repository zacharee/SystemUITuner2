package com.zacharee1.systemuituner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class RootActivity extends AppCompatActivity {
    SetThings setThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);

        setContentView(R.layout.activity_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.title_root);
        title.setTextColor(setThings.titleText);

        Button getPerms = (Button) findViewById(R.id.get_perms);
        setThings.buttons(getPerms, "setupDone");
    }
}
