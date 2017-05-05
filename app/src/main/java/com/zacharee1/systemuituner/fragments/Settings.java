package com.zacharee1.systemuituner.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;
import com.zacharee1.systemuituner.SetThings;

/**
 * Created by Zacha on 4/15/2017.
 */

public class Settings extends Fragment {
    private MainActivity activity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch darkMode = (Switch) view.findViewById(R.id.dark_mode);
        Switch safe_statbar = (Switch) view.findViewById(R.id.safe_statbar_method);
        Switch custom_settings = (Switch) view.findViewById(R.id.custom_settings_input);

        activity.setThings.switches(darkMode, null, "dark_mode", view);

        safe_statbar.setChecked(activity.setThings.sharedPreferences.getBoolean("safeStatbar", false));
        custom_settings.setChecked(activity.setThings.sharedPreferences.getBoolean("customSettings", false));

        switches(safe_statbar);
        switches(custom_settings);

        return view;
    }

    private void switches(final Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = toggle.getId();

                switch (id) {
                     case R.id.safe_statbar_method:
                        if (!isChecked && Build.MANUFACTURER.toLowerCase().contains("samsung")) {
                            new AlertDialog.Builder(view.getContext()) //warn about dangers of custom settings
                                    .setIcon(R.drawable.ic_warning_red)
                                    .setTitle(Html.fromHtml("<font color='#ff0000'>" + getResources().getText(R.string.warning) + "</font>"))
                                    .setMessage(getResources().getText(R.string.samsung_warning_message))
                                    .setPositiveButton(getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.setThings.editor.putBoolean("safeStatbar", false);
                                        }
                                    })
                                    .setNegativeButton(getResources().getText(R.string.no), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            toggle.setChecked(true);
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            activity.setThings.editor.putBoolean("safeStatbar", isChecked);
                        }
                        break;
                    case R.id.custom_settings_input:
                        if (isChecked) {
                            new AlertDialog.Builder(view.getContext()) //warn about dangers of custom settings
                                    .setIcon(R.drawable.ic_warning_red)
                                    .setTitle(Html.fromHtml("<font color='#ff0000'>" + getResources().getText(R.string.warning) + "</font>"))
                                    .setMessage(getResources().getText(R.string.custom_settings_warning))
                                    .setPositiveButton(getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.setThings.editor.putBoolean("customSettings", true);
                                        }
                                    })
                                    .setNegativeButton(getResources().getText(R.string.no), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            toggle.setChecked(false);
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            activity.setThings.editor.putBoolean("customSettings", false);
                        }
                        break;
                }

                activity.setThings.editor.apply();
            }
        });
    }
}