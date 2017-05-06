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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

import java.util.ArrayList;

/**
 * Created by Zacha on 4/5/2017.
 */

//@SuppressWarnings("ALL")
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

    private String SLOT_BLUETOOTH = "bluetooth";
    private String SLOT_DATA_SAVER = "data_saver";
    private String SLOT_ETHERNET = "ethernet";
    private String SLOT_HOTSPOT = "hotspot";
    private String SLOT_MOBILE = "mobile";
    private String SLOT_AIRPLANE = "airplane";
    private String SLOT_WIFI = "wifi";
    private String SLOT_VPN = "vpn";

    private String SLOT_VOLUME = "volume";
    private String SLOT_HEADSET = "headset";
    private String SLOT_SPEAKERPHONE = "speakerphone";

    private String SLOT_CLOCK = "clock";
    private String SLOT_ALARM = "alarm_clock,alarm";
    private String SLOT_ZEN = "zen";
    private String SLOT_DND = "do_not_disturb";

    private String SLOT_MAN_PROFILE = "managed_profile";
    private String SLOT_BATTERY = "battery";
    private String SLOT_CAST = "cast";
    private String SLOT_ROTATION = "rotate";
    private String SLOT_NFC = "nfc,nfc_on";
    private String SLOT_LOCATION = "location";
    private String SLOT_SU = "su";

    private String ICON_BLACKLIST = "icon_blacklist";
    private String RESET_BLACKLIST = "reset_blacklist";

    private Button reset_blacklist;

    private ArrayList<Switch> switches = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_statbar, container, false);

        drawable = R.drawable.ic_warning_red;

        isDark = activity.setThings.sharedPreferences.getBoolean("isDark", false);

        if (Build.MANUFACTURER.toUpperCase().contains("SAMSUNG") && !activity.setThings.sharedPreferences.getBoolean("samsungRisk", false)) { //show warning for Samsung users
            view.setVisibility(View.GONE);
            new AlertDialog.Builder(view.getContext())
                    .setIcon(drawable)
                    .setTitle(Html.fromHtml("<font color='#ff0000'>" + getResources().getText(R.string.warning) + "</font>"))
                    .setMessage(getResources().getText(R.string.samsung_warning_message))
                    .setPositiveButton(getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.setThings.editor.putBoolean("samsungRisk", true);
                            activity.setThings.editor.apply();
                            view.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton(getResources().getText(R.string.no), new DialogInterface.OnClickListener() {
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

        //set switch listeners
        activity.setThings.switches(bluetooth, SLOT_BLUETOOTH, ICON_BLACKLIST, view);
        activity.setThings.switches(wifi, SLOT_WIFI, ICON_BLACKLIST, view);
        activity.setThings.switches(ethernet, SLOT_ETHERNET, ICON_BLACKLIST, view);
        activity.setThings.switches(mobile, SLOT_MOBILE, ICON_BLACKLIST, view);
        activity.setThings.switches(airplane, SLOT_AIRPLANE, ICON_BLACKLIST, view);
        activity.setThings.switches(managed_profile, SLOT_MAN_PROFILE, ICON_BLACKLIST, view);
        activity.setThings.switches(zen, SLOT_ZEN, ICON_BLACKLIST, view);
        activity.setThings.switches(alarm_clock, SLOT_ALARM, ICON_BLACKLIST, view);
        activity.setThings.switches(hotspot, SLOT_HOTSPOT, ICON_BLACKLIST, view);
        activity.setThings.switches(data_saver, SLOT_DATA_SAVER, ICON_BLACKLIST, view);
        activity.setThings.switches(nfc, SLOT_NFC, ICON_BLACKLIST, view);
        activity.setThings.switches(clock, SLOT_CLOCK, ICON_BLACKLIST, view);
        activity.setThings.switches(do_not_disturb, SLOT_DND, ICON_BLACKLIST, view);
        activity.setThings.switches(rotate, SLOT_ROTATION, ICON_BLACKLIST, view);
        activity.setThings.switches(battery, SLOT_BATTERY, ICON_BLACKLIST, view);
        activity.setThings.switches(speakerphone, SLOT_SPEAKERPHONE, ICON_BLACKLIST, view);
        activity.setThings.switches(cast, SLOT_CAST, ICON_BLACKLIST, view);
        activity.setThings.switches(headset, SLOT_HEADSET, ICON_BLACKLIST, view);
        activity.setThings.switches(location, SLOT_LOCATION, ICON_BLACKLIST, view);
        activity.setThings.switches(su, SLOT_SU, ICON_BLACKLIST, view);
        activity.setThings.switches(vpn, SLOT_VPN, ICON_BLACKLIST, view);
        activity.setThings.switches(volume, SLOT_VOLUME, ICON_BLACKLIST, view);

        activity.setThings.buttons(reset_blacklist, RESET_BLACKLIST);

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
