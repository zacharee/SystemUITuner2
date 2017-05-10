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

    private final ArrayList<Switch> switches = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_statbar, container, false);

        int drawable = R.drawable.ic_warning_red;

        if ((Build.MANUFACTURER.toUpperCase().contains("SAMSUNG") || Build.MANUFACTURER.toUpperCase().contains("VIVO")) && !activity.setThings.sharedPreferences.getBoolean("samsungRisk", false)) { //show warning for Samsung and Vivo users
            view.setVisibility(View.GONE);
            //noinspection deprecation

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

        Switch vpn = (Switch) view.findViewById(R.id.vpn_icon);

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

        Button reset_blacklist = (Button) view.findViewById(R.id.reset_blacklist);

        //set switch listeners
        String ICON_BLACKLIST = "icon_blacklist";
        String SLOT_BLUETOOTH = "bluetooth";
        String SLOT_WIFI = "wifi";
        String SLOT_ETHERNET = "ethernet";
        String SLOT_MOBILE = "mobile";
        String SLOT_AIRPLANE = "airplane";
        String SLOT_MAN_PROFILE = "managed_profile";
        String SLOT_ZEN = "zen";
        String SLOT_ALARM = "alarm";
        String SLOT_ALARM_CLOCK = "alarm_clock";
        String SLOT_HOTSPOT = "hotspot";
        String SLOT_DATA_SAVER = "data_saver";
        String SLOT_NFC = "nfc";
        String SLOT_NFC_ON = "nfc_on";
        String SLOT_CLOCK = "clock";
        String SLOT_DND = "do_not_disturb";
        String SLOT_ROTATION = "rotate";
        String SLOT_BATTERY = "battery";
        String SLOT_SPEAKERPHONE = "speakerphone";
        String SLOT_CAST = "cast";
        String SLOT_HEADSET = "headset";
        String SLOT_LOCATION = "location";
        String SLOT_SU = "su";
        String SLOT_VPN = "vpn";
        String SLOT_VOLUME = "volume";
        String RESET_BLACKLIST = "reset_blacklist";

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

    private void checkBL(@SuppressWarnings("SameParameterValue") boolean tralse) {
        for (Switch toggle : switches) {
            toggle.setChecked(tralse);
        }
    }

}
