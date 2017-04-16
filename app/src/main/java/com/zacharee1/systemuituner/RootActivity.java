package com.zacharee1.systemuituner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("com.zacharee1.sysuituner", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isDark", false)) {
            setTheme(R.style.DARK_NoAppBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        setContentView(R.layout.activity_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button getPerms = (Button) findViewById(R.id.get_perms);

        buttons(getPerms);
    }

    public void buttons(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        sudo("pm grant com.zacharee1.systemuituner android.permission.DUMP ; pm grant com.zacharee1.systemuituner android.permission.WRITE_SECURE_SETTINGS");
                    }
                }).start();
            }
        });
    }

    public void sudo(String...strings) {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Great! Go play around!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("No Root?", e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "You sure you're rooted?", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            outputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}
