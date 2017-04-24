package com.zacharee1.systemuituner.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 4/5/2017.
 */

@SuppressWarnings("ALL")
public class StatBar extends Fragment {
    private View view;
    private MainActivity activity;

    private boolean isDark;

    private int drawable;

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
                            "Unrooted users are safe. " +
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
        Switch vpn = (Switch) view.findViewById(R.id.vpn_icon);

        //custom switch text
        battery_percent.setText(Html.fromHtml("Battery Percentage<br /><small> <font color=\"#777777\">(Reboot Required)</font></small>"));

        if (Build.VERSION.SDK_INT > 23) { //only show switch if user is on Nougat or later
            clock_seconds.setVisibility(View.VISIBLE);
        } else {
            clock_seconds.setVisibility(View.GONE);
        }

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
        activity.setThings.switches(nfc, "nfc", bl, view);
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

        activity.setThings.switches(clock_seconds, "clock_seconds", sec, view);

        activity.setThings.switches(battery_percent, "status_bar_show_battery_percent", sys, view);
        return view;
    }

}
