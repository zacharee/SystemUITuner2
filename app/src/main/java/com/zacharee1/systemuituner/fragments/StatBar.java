package com.zacharee1.systemuituner.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

import java.util.ArrayList;

/**
 * Created by Zacha on 4/5/2017.
 */

@SuppressWarnings("ALL")
public class StatBar extends Fragment {
    private View view;
    private MainActivity activity;

    private boolean isDark;

    private int drawable;

    private Switch bluetooth;
    private Switch wifi;
    private Switch ethernet;
    private Switch mobile;
    private Switch airplane;
    private Switch managed_profile;
    private Switch zen;
    private Switch alarm_clock;
    private Switch hotspot;
    private Switch data_saver;
    private Switch nfc;
    private Switch clock;
    private Switch volume;
    private Switch do_not_disturb;
    private Switch rotate;
    private Switch battery;
    private Switch speakerphone;
    private Switch cast;
    private Switch headset;
    private Switch location;
    private Switch su;
    private Switch vpn;

    private Button reset_blacklist;

    private ArrayList<Switch> switches = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("Status Bar"); //set proper fragment title

        view = inflater.inflate(R.layout.fragment_statbar, container, false);

        drawable = R.drawable.ic_warning_red;

        isDark = activity.setThings.sharedPreferences.getBoolean("isDark", false);

        if (Build.MANUFACTURER.toUpperCase().contains("SAMSUNG") && !activity.setThings.sharedPreferences.getBoolean("samsungRisk", false)) { //show warning for Samsung users
            view.setVisibility(View.GONE);
            new AlertDialog.Builder(view.getContext())
                    .setIcon(drawable)
                    .setTitle(Html.fromHtml("<font color='#ff0000'>WARNING</font>"))
                    .setMessage("It seems you are using a Samsung device. " +
                            "If you are on Stock (TouchWiz), and you're rooted, SystemUI may break if you use this feature! " +
                            "Unrooted users are most likely safe, but there's no guarantee! " +
                            "Are you sure you want to continue?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.setThings.editor.putBoolean("samsungRisk", true);
                            activity.setThings.editor.apply();
                            view.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Main fragment = new Main();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
                            NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_home).setChecked(true);
                        }
                    })
                    .show();
        }

        //define switches
        bluetooth = (Switch) view.findViewById(R.id.bt_icon);
        wifi = (Switch) view.findViewById(R.id.wifi_icon);
        ethernet = (Switch) view.findViewById(R.id.ethernet_icon);
        mobile = (Switch) view.findViewById(R.id.mobile_icon);
        airplane = (Switch) view.findViewById(R.id.airplane_icon);
        managed_profile = (Switch) view.findViewById(R.id.managed_profile_icon);
        zen = (Switch) view.findViewById(R.id.zen_icon);
        alarm_clock = (Switch) view.findViewById(R.id.alarm_clock_icon);
        hotspot = (Switch) view.findViewById(R.id.hotspot_icon);
        data_saver = (Switch) view.findViewById(R.id.data_saver);
        nfc = (Switch) view.findViewById(R.id.nfc);
        clock = (Switch) view.findViewById(R.id.clock);
        volume = (Switch) view.findViewById(R.id.volume);
        do_not_disturb = (Switch) view.findViewById(R.id.do_not_disturb);
        rotate = (Switch) view.findViewById(R.id.rotate);
        battery = (Switch) view.findViewById(R.id.battery);
        speakerphone = (Switch) view.findViewById(R.id.speakerphone);
        cast = (Switch) view.findViewById(R.id.cast);
        headset = (Switch) view.findViewById(R.id.headset);
        location = (Switch) view.findViewById(R.id.location);
        su = (Switch) view.findViewById(R.id.su);

        vpn = (Switch) view.findViewById(R.id.vpn_icon);

        switches.add(bluetooth);
        switches.add(wifi);
        switches.add(ethernet);
        switches.add(mobile);
        switches.add(airplane);
        switches.add(managed_profile);
        switches.add(zen);
        switches.add(alarm_clock);
        switches.add(hotspot);
        switches.add(data_saver);
        switches.add(nfc);
        switches.add(clock);
        switches.add(volume);
        switches.add(do_not_disturb);
        switches.add(rotate);
        switches.add(battery);
        switches.add(speakerphone);
        switches.add(cast);
        switches.add(headset);
        switches.add(location);
        switches.add(su);
        switches.add(vpn);

        reset_blacklist = (Button) view.findViewById(R.id.reset_blacklist);

        //strings because I'm lazy
        String bl = "icon_blacklist";
        String sec = "secure";
        String sys = "system";
        String glob = "global";

        //set switch listeners
        activity.setThings.switches(bluetooth, "bluetooth", bl, view);
        activity.setThings.switches(wifi, "wifi", bl, view);
        activity.setThings.switches(ethernet, "ethernet", bl, view);
        activity.setThings.switches(mobile, "mobile", bl, view);
        activity.setThings.switches(airplane, "airplane", bl, view);
        activity.setThings.switches(managed_profile, "managed_profile", bl, view);
        activity.setThings.switches(zen, "zen", bl, view);
        activity.setThings.switches(alarm_clock, "alarm_clock,alarm", bl, view);
        activity.setThings.switches(hotspot, "hotspot", bl, view);
        activity.setThings.switches(data_saver, "data_saver", bl, view);
        activity.setThings.switches(nfc, "nfc,nfc_on", bl, view);
        activity.setThings.switches(clock, "clock", bl, view);
        activity.setThings.switches(do_not_disturb, "do_not_disturb", bl, view);
        activity.setThings.switches(rotate, "rotate", bl, view);
        activity.setThings.switches(battery, "battery", bl, view);
        activity.setThings.switches(speakerphone, "speakerphone", bl, view);
        activity.setThings.switches(cast, "cast", bl, view);
        activity.setThings.switches(headset, "headset", bl, view);
        activity.setThings.switches(location, "location", bl, view);
        activity.setThings.switches(su, "su", bl, view);
        activity.setThings.switches(vpn, "vpn", bl, view);
        activity.setThings.switches(volume, "volume", bl, view);

        activity.setThings.buttons(reset_blacklist, "reset_blacklist");

        BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("check_statbar_toggles")) {
                    checkBL(true);
                }
            }
        };
        activity.registerReceiver(broadcast_reciever, new IntentFilter("check_statbar_toggles"));

        return view;
    }

    private void checkBL(boolean tralse) {
        for (Switch toggle : switches) {
            toggle.setChecked(tralse);
        }
    }

}
