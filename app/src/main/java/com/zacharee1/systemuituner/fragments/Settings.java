package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;
import com.zacharee1.systemuituner.Utils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Zacha on 4/15/2017.
 */

public class Settings extends Fragment {
    public View view;
    public MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("Settings");

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView title = (TextView) view.findViewById(R.id.title_settings);

        if (activity.setThings.sharedPreferences.getBoolean("isDark", false)) {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        Switch darkMode = (Switch) view.findViewById(R.id.dark_mode);
        LinearLayout settings = (LinearLayout) view.findViewById(R.id.settings);

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

        Drawable background;

        if (activity.setThings.sharedPreferences.getBoolean("isDark", false)) {
            background = activity.getDrawable(R.drawable.layout_bg_dark);
            title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            background = activity.getDrawable(R.drawable.layout_bg_light);
            title.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

//        settings.setBackground(background);

        return view;
    }
}
