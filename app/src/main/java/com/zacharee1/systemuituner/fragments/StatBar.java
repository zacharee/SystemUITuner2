package com.zacharee1.systemuituner.fragments;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
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
@SuppressWarnings("FieldCanBeLocal")
public class StatBar extends Fragment {
    private View view;
    private MainActivity activity;

    private final ArrayList<Switch> switches = new ArrayList<>();
    private BroadcastReceiver check_toggles_receiver;

    private static final String ICON_BLACKLIST = "icon_blacklist";
    private static final String SLOT_BLUETOOTH = "bluetooth";
    private static final String SLOT_WIFI = "wifi";
    private static final String SLOT_ETHERNET = "ethernet";
    private static final String SLOT_MOBILE = "mobile";
    private static final String SLOT_AIRPLANE = "airplane";
    private static final String SLOT_MAN_PROFILE = "managed_profile";
    private static final String SLOT_ZEN = "zen";
    private static final String SLOT_ALARM = "alarm,alarm_clock";
    private static final String SLOT_HOTSPOT = "hotspot";
    private static final String SLOT_DATA_SAVER = "data_saver";
    private static final String SLOT_NFC = "nfc,nfc_on";
    private static final String SLOT_CLOCK = "clock";
    private static final String SLOT_DND = "do_not_disturb";
    private static final String SLOT_ROTATION = "rotate";
    private static final String SLOT_BATTERY = "battery";
    private static final String SLOT_SPEAKERPHONE = "speakerphone";
    private static final String SLOT_CAST = "cast";
    private static final String SLOT_HEADSET = "headset";
    private static final String SLOT_LOCATION = "location";
    private static final String SLOT_SU = "su";
    private static final String SLOT_VPN = "vpn";
    private static final String SLOT_VOLUME = "volume";

    private static final String SLOT_REMOTE_CALL = "remote_call";
    private static final String SLOT_OTG_MOUSE = "otg_mouse";
    private static final String SLOT_OTG_KEYBOARD = "otg_keyboard";
    private static final String SLOT_DMB = "dmb";
    private static final String SLOT_FELICA_LOCK = "felica_lock";
    private static final String SLOT_ANSWERING_MEMO = "answering_memo";
    private static final String SLOT_IME = "ime";
    private static final String SLOT_SYNC_FAILING = "sync_failing";
    private static final String SLOT_SYNC_ACTIVE = "sync_active";
    private static final String SLOT_NFCLOCK = "nfclock";
    private static final String SLOT_TTY = "tty";
    private static final String SLOT_WIFI_CALLING = "wifi_calling";
    private static final String SLOT_CDMA_ERI = "cdma_eri";
    private static final String SLOT_DATA_CONNECTION = "data_connection";
    private static final String SLOT_PHONE_EVDO_SIGNAL = "phone_evdo_signal";
    private static final String SLOT_PHONE_SIGNAL = "phone_signal";
    private static final String SLOT_SECURE = "secure";
    private static final String SLOT_VOLTE = "volte";

    private static final String SLOT_VOWIFI = "vowifi";
    private static final String SLOT_POWER_SAVER = "power_saver";

    private static final String RESET_BLACKLIST = "reset_blacklist";

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
                            NavigationView navigationView = activity.findViewById(R.id.nav_view);
                            Menu navMenu = navigationView.getMenu();
                            navMenu.findItem(R.id.nav_home).setChecked(true);
                        }
                    })
                    .show();
        }

        //define switches
        Switch bluetooth = view.findViewById(R.id.bt_icon);
        Switch wifi = view.findViewById(R.id.wifi_icon);
        Switch ethernet = view.findViewById(R.id.ethernet_icon);
        Switch mobile = view.findViewById(R.id.mobile_icon);
        Switch airplane = view.findViewById(R.id.airplane_icon);
        Switch managed_profile = view.findViewById(R.id.managed_profile_icon);
        Switch zen = view.findViewById(R.id.zen_icon);
        Switch alarm_clock = view.findViewById(R.id.alarm_clock_icon);
        Switch hotspot = view.findViewById(R.id.hotspot_icon);
        Switch data_saver = view.findViewById(R.id.data_saver);
        Switch nfc = view.findViewById(R.id.nfc);
        Switch clock = view.findViewById(R.id.clock);
        Switch volume = view.findViewById(R.id.volume);
        Switch do_not_disturb = view.findViewById(R.id.do_not_disturb);
        Switch rotate = view.findViewById(R.id.rotate);
        Switch battery = view.findViewById(R.id.battery);
        Switch speakerphone = view.findViewById(R.id.speakerphone);
        Switch cast = view.findViewById(R.id.cast);
        Switch headset = view.findViewById(R.id.headset);
        Switch location = view.findViewById(R.id.location);
        Switch su = view.findViewById(R.id.su);

        Switch vpn = view.findViewById(R.id.vpn_icon);

        Switch remote_call = view.findViewById(R.id.remote_call);
        Switch volte = view.findViewById(R.id.volte);
        Switch vowifi = view.findViewById(R.id.vowifi);
        Switch dmb = view.findViewById(R.id.dmb);
        Switch tty = view.findViewById(R.id.tty);
        Switch wifi_calling = view.findViewById(R.id.wifi_calling);
        Switch cdma_eri = view.findViewById(R.id.cdma_eri);
        Switch data_connection = view.findViewById(R.id.data_connection);
        Switch phone_evdo_signal = view.findViewById(R.id.phone_evdo_signal);
        Switch phone_signal = view.findViewById(R.id.phone_signal);
        Switch otg_mouse = view.findViewById(R.id.otg_mouse);
        Switch otg_keyboard = view.findViewById(R.id.otg_keyboard);
        Switch felica_lock = view.findViewById(R.id.felica_lock);
        Switch answering_memo = view.findViewById(R.id.answering_memo);
        Switch ime = view.findViewById(R.id.ime);
        Switch sync_failing = view.findViewById(R.id.sync_failing);
        Switch sync_active = view.findViewById(R.id.sync_active);
        Switch nfclock = view.findViewById(R.id.nfclock);
        Switch secure = view.findViewById(R.id.secure);
        Switch power_saver = view.findViewById(R.id.power_saver);

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

        switches.add(remote_call);
        switches.add(volte);
        switches.add(vowifi);
        switches.add(dmb);
        switches.add(tty);
        switches.add(wifi_calling);
        switches.add(cdma_eri);
        switches.add(data_connection);
        switches.add(phone_evdo_signal);
        switches.add(phone_signal);
        switches.add(otg_mouse);
        switches.add(otg_keyboard);
        switches.add(felica_lock);
        switches.add(answering_memo);
        switches.add(ime);
        switches.add(sync_failing);
        switches.add(sync_active);
        switches.add(nfclock);
        switches.add(secure);
        switches.add(power_saver);

        Button reset_blacklist = view.findViewById(R.id.reset_blacklist);

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

        activity.setThings.switches(volte, SLOT_VOLTE, ICON_BLACKLIST, view);
        activity.setThings.switches(vowifi, SLOT_VOWIFI, ICON_BLACKLIST, view);
        activity.setThings.switches(remote_call, SLOT_REMOTE_CALL, ICON_BLACKLIST, view);
        activity.setThings.switches(dmb, SLOT_DMB, ICON_BLACKLIST, view);
        activity.setThings.switches(tty, SLOT_TTY, ICON_BLACKLIST, view);
        activity.setThings.switches(wifi_calling, SLOT_WIFI_CALLING, ICON_BLACKLIST, view);
        activity.setThings.switches(cdma_eri, SLOT_CDMA_ERI, ICON_BLACKLIST, view);
        activity.setThings.switches(data_connection, SLOT_DATA_CONNECTION, ICON_BLACKLIST, view);
        activity.setThings.switches(phone_evdo_signal, SLOT_PHONE_EVDO_SIGNAL, ICON_BLACKLIST, view);
        activity.setThings.switches(phone_signal, SLOT_PHONE_SIGNAL, ICON_BLACKLIST, view);
        activity.setThings.switches(otg_mouse, SLOT_OTG_MOUSE, ICON_BLACKLIST, view);
        activity.setThings.switches(otg_keyboard, SLOT_OTG_KEYBOARD, ICON_BLACKLIST, view);
        activity.setThings.switches(felica_lock, SLOT_FELICA_LOCK, ICON_BLACKLIST, view);
        activity.setThings.switches(answering_memo, SLOT_ANSWERING_MEMO, ICON_BLACKLIST, view);
        activity.setThings.switches(ime, SLOT_IME, ICON_BLACKLIST, view);
        activity.setThings.switches(sync_failing, SLOT_SYNC_FAILING, ICON_BLACKLIST, view);
        activity.setThings.switches(sync_active, SLOT_SYNC_ACTIVE, ICON_BLACKLIST, view);
        activity.setThings.switches(nfclock, SLOT_NFCLOCK, ICON_BLACKLIST, view);
        activity.setThings.switches(secure, SLOT_SECURE, ICON_BLACKLIST, view);
        activity.setThings.switches(power_saver, SLOT_POWER_SAVER, ICON_BLACKLIST, view);

        activity.setThings.buttons(reset_blacklist, RESET_BLACKLIST);

        check_toggles_receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("check_statbar_toggles")) {
                    checkBL(true);
                }
            }
        };
        activity.registerReceiver(check_toggles_receiver, new IntentFilter("check_statbar_toggles"));

        return view;
    }

    @Override
    public void onDestroy() {
        activity.unregisterReceiver(check_toggles_receiver);
        super.onDestroy();
    }

    private void checkBL(@SuppressWarnings("SameParameterValue") boolean tralse) {
        for (Switch toggle : switches) {
            toggle.setChecked(tralse);
        }
    }

}
