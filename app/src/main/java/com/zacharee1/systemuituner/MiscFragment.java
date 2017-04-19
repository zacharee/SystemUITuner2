package com.zacharee1.systemuituner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Zacha on 4/18/2017.
 */

public class MiscFragment extends Fragment {
    public View view;
    public MainActivity activity;

    Switch show_full_zen;

    int drawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_misc, container, false);

        drawable = R.drawable.ic_warning_red;

        TextView title = (TextView) view.findViewById(R.id.title_misc);
        LinearLayout stuff = (LinearLayout) view.findViewById(R.id.stuff);
        Drawable background;

        if (activity.sharedPreferences.getBoolean("isDark", false)) {
            background = activity.getDrawable(R.drawable.layout_bg_dark);
            title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            background = activity.getDrawable(R.drawable.layout_bg_light);
            title.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        stuff.setBackground(background);

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
                                Settings.System.putInt(activity.getContentResolver(), pref, 1);
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
                                Settings.System.putInt(activity.getContentResolver(), pref, 0);
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
