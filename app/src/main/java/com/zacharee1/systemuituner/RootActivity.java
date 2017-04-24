package com.zacharee1.systemuituner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class RootActivity extends AppCompatActivity {
    private SetThings setThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);

        setContentView(R.layout.activity_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.title_root);
        title.setTextColor(setThings.titleText); //set proper title text color

        Button getPerms = (Button) findViewById(R.id.get_perms);
        setThings.buttons(getPerms, "setupDoneRoot"); //button listener
    }
}
