package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;
import com.zacharee1.systemuituner.Utils;

/**
 * Created by Zacha on 4/15/2017.
 */

public class Settings extends Fragment {
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("Settings");

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch darkMode = (Switch) view.findViewById(R.id.dark_mode);

        if (activity.setThings.sharedPreferences.getBoolean("isDark", false)) {
            darkMode.setChecked(true);
        }

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.setThings.editor.putBoolean("isDark", true);
                    Utils.changeToTheme(activity, 1);

                } else {
                    activity.setThings.editor.putBoolean("isDark", false);
                    Utils.changeToTheme(activity, 0);
                }
                activity.setThings.editor.apply();
            }
        });

        return view;
    }
}
