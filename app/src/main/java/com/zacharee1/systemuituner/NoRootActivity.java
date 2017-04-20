package com.zacharee1.systemuituner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NoRootActivity extends AppCompatActivity {
    SetThings setThings;

    Button setupDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);

        setContentView(R.layout.activity_no_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.noroot_title);

        title.setTextColor(setThings.titleText);

        setupDone = (Button) findViewById(R.id.no_root_done);

        setThings.buttons(setupDone, "setupDone");
    }

}
