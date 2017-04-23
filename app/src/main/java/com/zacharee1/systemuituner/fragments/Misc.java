package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zacharee1.systemuituner.Exceptions;
import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 4/18/2017.
 */

@SuppressWarnings("ALL")
public class Misc extends Fragment {
    private View view;
    private MainActivity activity;

    private Switch show_full_zen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("Miscellaneous");

        view = inflater.inflate(R.layout.fragment_misc, container, false);

        show_full_zen = (Switch) view.findViewById(R.id.show_full_zen);

        switches(show_full_zen, "sysui_show_full_zen", "secure");

        return view;
    }

    private void switches(final Switch toggle, final String pref, final String settingType) {
        int setting = 0;
        switch (settingType) {
            case "global":
                setting = Settings.Global.getInt(activity.getContentResolver(), pref, 0);
                break;
            case "secure":
                setting = Settings.Secure.getInt(activity.getContentResolver(), pref, 0);
                break;
            case "system":
                setting = Settings.System.getInt(activity.getContentResolver(), pref, 0);
                break;
        }
        toggle.setChecked(setting == 1);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        switch (settingType) {
                            case "global":
                                Settings.Global.putInt(activity.getContentResolver(), pref, 1);
                                break;
                            case "secure":
                                Settings.Secure.putInt(activity.getContentResolver(), pref, 1);
                                break;
                            case "system":
                                Runtime.getRuntime().exec("content insert --uri content://settings/system --bind name:s:" + pref + " --bind value:i:1");
                                break;
                        }
                    } else {
                        switch (settingType) {
                            case "global":
                                Settings.Global.putInt(activity.getContentResolver(), pref, 0);
                                break;
                            case "secure":
                                Settings.Secure.putInt(activity.getContentResolver(), pref, 0);
                                break;
                            case "system":
                                Runtime.getRuntime().exec("content insert --uri content://settings/system --bind name:s:" + pref + " --bind value:i:1");
                                break;
                        }
                    }
                } catch (Exception e) {
                    Exceptions exceptions = new Exceptions();
                    exceptions.secureSettings(view.getContext(), activity.getApplicationContext(), e.getMessage(), "Misc");
                }
            }
        });
    }
}
