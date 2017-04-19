package com.zacharee1.systemuituner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Zacha on 4/5/2017.
 */

public class StatBarFragment extends Fragment {
    public View view;
    public MainActivity activity;

    boolean isDark;

    int drawable;

    Exceptions exceptions = new Exceptions();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_statbar, container, false);

        drawable = R.drawable.ic_warning_red;

        isDark = activity.sharedPreferences.getBoolean("isDark", false);

        if (Build.MANUFACTURER.toUpperCase().contains("SAMSUNG") && !activity.sharedPreferences.getBoolean("samsungRisk", false)) {
            view.setVisibility(View.GONE);
            new AlertDialog.Builder(view.getContext())
                    .setIcon(drawable)
                    .setTitle(Html.fromHtml("<font color='#ff0000'>WARNING</font>"))
                    .setMessage("It seems you are using a Samsung device. " +
                            "If you are on Stock (TouchWiz), and you're rooted, SystemUI may break if you use this feature! " +
                            "Unrooted users are safe. " +
                            "Are you sure you want to continue?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.editor.putBoolean("samsungRisk", true);
                            activity.editor.apply();
                            view.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainFragment fragment = new MainFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
                            NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_home).setChecked(true);
                        }
                    })
                    .show();
        }

        Switch bluetooth = (Switch) view.findViewById(R.id.bt_icon);
        Switch wifi = (Switch) view.findViewById(R.id.wifi_icon);
        Switch ethernet = (Switch) view.findViewById(R.id.ethernet_icon);
        Switch mobile = (Switch) view.findViewById(R.id.mobile_icon);
        Switch airplane = (Switch) view.findViewById(R.id.airplane_icon);
        Switch managed_profile = (Switch) view.findViewById(R.id.managed_profile_icon);
        Switch zen = (Switch) view.findViewById(R.id.zen_icon);
        Switch alarm_clock = (Switch) view.findViewById(R.id.alarm_clock_icon);
        Switch hotspot = (Switch) view.findViewById(R.id.hotspot_icon);
        Switch data_saver = (Switch) view.findViewById(R.id.data_saver);
        Switch nfc = (Switch) view.findViewById(R.id.nfc);
        Switch clock = (Switch) view.findViewById(R.id.clock);
        Switch volume = (Switch) view.findViewById(R.id.volume);
        Switch do_not_disturb = (Switch) view.findViewById(R.id.do_not_disturb);
        Switch rotate = (Switch) view.findViewById(R.id.rotate);
        Switch battery = (Switch) view.findViewById(R.id.battery);
        Switch speakerphone = (Switch) view.findViewById(R.id.speakerphone);
        Switch cast = (Switch) view.findViewById(R.id.cast);
        Switch headset = (Switch) view.findViewById(R.id.headset);
        Switch location = (Switch) view.findViewById(R.id.location);
        Switch su = (Switch) view.findViewById(R.id.su);
        Switch clock_seconds = (Switch) view.findViewById(R.id.clock_seconds);
        Switch battery_percent = (Switch) view.findViewById(R.id.battery_percent);

        battery_percent.setText(Html.fromHtml("Battery Percentage<br /><small> <font color=\"#777777\">(Reboot Required)</font></small>"));

        if (Build.VERSION.SDK_INT > 23) {
            clock_seconds.setVisibility(View.VISIBLE);
        } else {
            clock_seconds.setVisibility(View.GONE);
        }

        LinearLayout network = (LinearLayout) view.findViewById(R.id.network);
        LinearLayout sound = (LinearLayout) view.findViewById(R.id.sound);
        LinearLayout misc = (LinearLayout) view.findViewById(R.id.misc);
        LinearLayout time = (LinearLayout) view.findViewById(R.id.time);

        TextView title = (TextView) view.findViewById(R.id.title_stat);

        Drawable background;

        if (isDark) {
            background = activity.getDrawable(R.drawable.layout_bg_dark);
            title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            background = activity.getDrawable(R.drawable.layout_bg_light);
            title.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        network.setBackground(background);
        sound.setBackground(background);
        misc.setBackground(background);
        time.setBackground(background);

        String bl = "icon_blacklist";
        String sec = "secure";
        String sys = "system";
        String glob = "global";

        sharedPrefs("bluetooth", bluetooth, bl);
        sharedPrefs("wifi", wifi, bl);
        sharedPrefs("ethernet", ethernet, bl);
        sharedPrefs("mobile", mobile, bl);
        sharedPrefs("airplane", airplane, bl);
        sharedPrefs("managed_profile", managed_profile, bl);
        sharedPrefs("zen", zen, bl);
        sharedPrefs("alarm_clock", alarm_clock, bl);
        sharedPrefs("hotspot", hotspot, bl);
        sharedPrefs("data_saver", data_saver, bl);
        sharedPrefs("nfc", nfc, bl);
        sharedPrefs("clock", clock, bl);
        sharedPrefs("volume", volume, bl);
        sharedPrefs("do_not_disturb", do_not_disturb, bl);
        sharedPrefs("rotate", rotate, bl);
        sharedPrefs("battery", battery, bl);
        sharedPrefs("speakerphone", speakerphone, bl);
        sharedPrefs("cast", cast, bl);
        sharedPrefs("headset", headset, bl);
        sharedPrefs("location", location, bl);
        sharedPrefs("su", su, bl);

        sharedPrefs("clock_seconds", clock_seconds, sec);

        sharedPrefs("status_bar_show_battery_percent", battery_percent, sys);

        switches(bluetooth, "bluetooth", bl);
        switches(wifi, "wifi", bl);
        switches(ethernet, "ethernet", bl);
        switches(mobile, "mobile", bl);
        switches(airplane, "airplane", bl);
        switches(managed_profile, "managed_profile", bl);
        switches(zen, "zen", bl);
        switches(alarm_clock, "alarm_clock,alarm", bl);
        switches(hotspot, "hotspot", bl);
        switches(data_saver, "data_saver", bl);
        switches(nfc, "nfc", bl);
        switches(clock, "clock", bl);
        switches(volume, "volume", bl);
        switches(do_not_disturb, "do_not_disturb", bl);
        switches(rotate, "rotate", bl);
        switches(battery, "battery", bl);
        switches(speakerphone, "speakerphone", bl);
        switches(cast, "cast", bl);
        switches(headset, "headset", bl);
        switches(location, "location", bl);
        switches(su, "su", bl);

        switches(clock_seconds, "clock_seconds", sec);

        switches(battery_percent, "status_bar_show_battery_percent", sys);
        return view;
    }

    public void sharedPrefs(String key, Switch toggle, String prefType) {
        int enabled = 0;
        switch (prefType) {
            case "global":
                break;
            case "secure":
                enabled = Settings.Secure.getInt(activity.getContentResolver(), key, 0);
                break;
            case "system":
                enabled = Settings.System.getInt(activity.getContentResolver(), key, 0);
                break;
            case "icon_blacklist":
                String blacklist = Settings.Secure.getString(activity.getContentResolver(), "icon_blacklist") != null ? Settings.Secure.getString(activity.getContentResolver(), "icon_blacklist") : "nada";
                enabled = !blacklist.contains(key) ? 1 : 0;
                break;
        }
        toggle.setChecked(enabled == 1);

    }

    public void switches(Switch toggle, final String setting, final String type) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                try {
                    switch (type) {
                        case "icon_blacklist":
                            String blacklist = Settings.Secure.getString(activity.getContentResolver(), "icon_blacklist");
                            if (!isChecked) {
                                if (blacklist != null && !blacklist.equals("")) {
                                    blacklist = blacklist.concat("," + setting);
                                } else {
                                    blacklist = setting;
                                }
                                activity.editor.putBoolean(setting, false);
                            } else {
                                if (blacklist != null) {
                                    blacklist = blacklist.replace("," + setting, "");
                                    blacklist = blacklist.replace(setting, "");
                                }
                                activity.editor.putBoolean(setting, true);
                            }

                            activity.editor.apply();
                            final String blacklist2 = blacklist;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    try {
                                        if (blacklist2 != null && !blacklist2.equals("")) {
                                            try {
                                                Settings.Secure.putString(activity.getContentResolver(), "icon_blacklist", blacklist2);
                                            } catch (final Exception e) {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        exceptions.secureSettings(view.getContext(), activity.getApplicationContext(), e.getMessage(), "icon_blacklist");
                                                    }
                                                });
                                            }
                                        } else {
                                            try {
                                                Settings.Secure.putString(activity.getContentResolver(), "icon_blacklist", "");
                                            } catch (final Exception e) {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        exceptions.secureSettings(view.getContext(), activity.getApplicationContext(), e.getMessage(), "icon_blacklist");
                                                    }
                                                });
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.e("idk", e.getMessage());
                                    }
                                }
                            }).start();
                            break;
                        case "system":
                            if (isChecked) {
                                Settings.System.putInt(activity.getContentResolver(), setting, 1);
                                Runtime.getRuntime().exec("content insert --uri content://settings/system --bind name:s:" + setting + " --bind value:i:1");
                            } else {
                                Settings.System.putInt(activity.getContentResolver(), setting, 0);
                                Runtime.getRuntime().exec("content insert --uri content://settings/system --bind name:s:" + setting + " --bind value:i:0");
                            }
                            break;
                        case "secure":
                            if (isChecked) {
                                Settings.Secure.putInt(activity.getContentResolver(), setting, 1);
                            } else {
                                Settings.Secure.putInt(activity.getContentResolver(), setting, 0);
                            }
                            break;
                        case "global":
                            break;
                    }
                } catch (Exception e) {
                    Exceptions exceptions = new Exceptions();
                    exceptions.secureSettings(view.getContext(), activity.getApplicationContext(), e.getMessage(), "Status Bar");
                }
            }
        });
    }
}
