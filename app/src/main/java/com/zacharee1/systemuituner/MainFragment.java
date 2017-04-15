package com.zacharee1.systemuituner;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Zacha on 4/2/2017.
 */

public class MainFragment extends Fragment {
    public View view;
    public MainActivity activity;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button rooted;
    Button no_root;

    TextView rootStat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_main, container, false);

        sharedPreferences = activity.sharedPreferences;
        editor = sharedPreferences.edit();

        TextView title = (TextView) view.findViewById(R.id.title_main);

        if (activity.sharedPreferences.getBoolean("isDark", false)) {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        rooted = (Button) view.findViewById(R.id.rooted);
        no_root = (Button) view.findViewById(R.id.not_rooted);

        rootStat = (TextView) view.findViewById(R.id.currently_rooted);
        rootStat();

        buttons(rooted);
        buttons(no_root);

        return view;
    }

    public void buttons(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button == rooted) {
                    try {
                        sudo("pm grant com.zacharee1.systemuituner android.permission.DUMP ; pm grant com.zacharee1.systemuituner android.permission.WRITE_SECURE_SETTINGS");
                        Toast.makeText(activity.getApplicationContext(), "Great! Go play around!", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("isRooted", true);
                    } catch (Exception e) {
                        Log.e("No Root?", e.getMessage());
                        Toast.makeText(activity.getApplicationContext(), "You sure you're rooted?", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("isRooted", false);
                    }
                } else if (button == no_root) {
                    Intent intent = new Intent(activity.getApplicationContext(), NoRoot.class);
                    startActivity(intent);
                    editor.putBoolean("isRooted", false);
                }
                editor.apply();
                rootStat();
            }
        });
    }

    public void rootStat() {
        boolean root = sharedPreferences.getBoolean("isRooted", false);

        if (root) {
            rootStat.setText("TRUE");
            rootStat.setTextColor(Color.rgb(0, 255, 0));
        } else {
            rootStat.setText("FALSE");
            rootStat.setTextColor(Color.rgb(255, 0, 0));
        }
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
