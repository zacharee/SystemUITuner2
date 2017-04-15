package com.zacharee1.systemuituner;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Zacha on 4/5/2017.
 */

public class StatBarFragment extends Fragment {
    public View view;
    public MainActivity activity;
    public SharedPreferences.Editor editor;

    boolean isRooted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_statbar, container, false);

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

        editor = activity.sharedPreferences.edit();

        isRooted = activity.sharedPreferences.getBoolean("isRooted", false);

        sharedPrefs("bluetooth", bluetooth);
        sharedPrefs("wifi", wifi);
        sharedPrefs("ethernet", ethernet);
        sharedPrefs("mobile", mobile);
        sharedPrefs("airplane", airplane);
        sharedPrefs("managed_profile", managed_profile);
        sharedPrefs("zen", zen);
        sharedPrefs("alarm_clock", alarm_clock);
        sharedPrefs("hotspot", hotspot);
        sharedPrefs("data_saver", data_saver);
        sharedPrefs("nfc", nfc);
        sharedPrefs("clock", clock);
        sharedPrefs("volume", volume);
        sharedPrefs("do_not_disturb", do_not_disturb);
        sharedPrefs("rotate", rotate);
        sharedPrefs("battery", battery);

        switches(bluetooth, "bluetooth");
        switches(wifi, "wifi");
        switches(ethernet, "ethernet");
        switches(mobile, "mobile");
        switches(airplane, "airplane");
        switches(managed_profile, "managed_profile");
        switches(zen, "zen");
        switches(alarm_clock, "alarm_clock");
        switches(hotspot, "hotspot");
        switches(data_saver, "data_saver");
        switches(nfc, "nfc");
        switches(clock, "clock");
        switches(volume, "volume");
        switches(do_not_disturb, "do_not_disturb");
        switches(rotate, "rotate");
        switches(battery, "battery");
        return view;
    }

    public void sharedPrefs(String key, Switch toggle) {
        if (activity.sharedPreferences.getBoolean(key, true)) {
            toggle.setChecked(true);
        }
    }

    public void switches(Switch toggle, final String setting) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                String blacklist = Settings.Secure.getString(activity.getContentResolver(), "icon_blacklist");
//                blacklist = Settings.System.getString(activity.getContentResolver(), "icon_blacklist");
                if (!isChecked) {
                    if (blacklist != null && blacklist != "") {
                        blacklist = blacklist.concat("," + setting);
                    } else {
                        blacklist = setting;
                    }
                    editor.putBoolean(setting, false);
//                    Settings.Secure.putString(activity.getContentResolver(), "icon_blacklist", blacklist);
                } else {
                    if (blacklist != null) {
                        blacklist = blacklist.replace("," + setting, "");
                        blacklist = blacklist.replace(setting, "");
                    }
                    editor.putBoolean(setting, true);
//                    Settings.Secure.putString(activity.getContentResolver(), "icon_blacklist", blacklist);
                }

                editor.apply();
                final String blacklist2 = blacklist;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (blacklist2 != "" && blacklist2 != null) {
//                                if (isRooted) sudo("settings put secure icon_blacklist " + blacklist2);
                                try {
                                    Settings.Secure.putString(activity.getContentResolver(), "icon_blacklist", blacklist2);
                                } catch (Exception e) {
                                    Log.e("icon_blacklist", e.getMessage());
                                    Toast.makeText(activity.getApplicationContext(), "Did you set up ADB?", Toast.LENGTH_LONG).show();
                                }
                            } else {
//                                if (isRooted) sudo("settings delete secure icon_blacklist");
                                try {
                                    Settings.Secure.putString(activity.getContentResolver(), "icon_blacklist", "");
                                } catch (Exception e) {
                                    Log.e("icon_blacklist", e.getMessage());
                                    Toast.makeText(activity.getApplicationContext(), "Did you set up ADB?", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {}
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
