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
    private BroadcastReceiver check_toggles_receiver;

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

        Switch knox_container = (Switch) view.findViewById(R.id.knox_container);
        Switch smart_network = (Switch) view.findViewById(R.id.smart_network);
        Switch glove = (Switch) view.findViewById(R.id.glove);
        Switch gesture = (Switch) view.findViewById(R.id.gesture);
        Switch smart_scroll = (Switch) view.findViewById(R.id.smart_scroll);
        Switch face = (Switch) view.findViewById(R.id.face);
        Switch gps = (Switch) view.findViewById(R.id.gps);
        Switch lbs = (Switch) view.findViewById(R.id.lbs);
        Switch wearable_gear = (Switch) view.findViewById(R.id.wearable_gear);
        Switch femtoicon = (Switch) view.findViewById(R.id.femtoicon);
        Switch rcs = (Switch) view.findViewById(R.id.rcs);
        Switch wifi_p2p = (Switch) view.findViewById(R.id.wifi_p2p);
        Switch wifi_ap = (Switch) view.findViewById(R.id.wifi_ap);
        Switch wifi_oxygen = (Switch) view.findViewById(R.id.wifi_oxygen);
        Switch phone_signal_second_stub = (Switch) view.findViewById(R.id.phone_signal_second_stub);
        Switch toddler = (Switch) view.findViewById(R.id.toddler);
        Switch ims_volte = (Switch) view.findViewById(R.id.ims_volte);
        Switch keyguard_wakeup = (Switch) view.findViewById(R.id.keyguard_wakeup);
        Switch safezone = (Switch) view.findViewById(R.id.safezone);
        Switch wimax = (Switch) view.findViewById(R.id.wimax);
        Switch smart_bonding = (Switch) view.findViewById(R.id.smart_bonding);
        Switch private_mode = (Switch) view.findViewById(R.id.private_mode);

        Switch remote_call = (Switch) view.findViewById(R.id.remote_call);
        Switch volte = (Switch) view.findViewById(R.id.volte);
        Switch vowifi = (Switch) view.findViewById(R.id.vowifi);
        Switch dmb = (Switch) view.findViewById(R.id.dmb);
        Switch tty = (Switch) view.findViewById(R.id.tty);
        Switch wifi_calling = (Switch) view.findViewById(R.id.wifi_calling);
        Switch cdma_eri = (Switch) view.findViewById(R.id.cdma_eri);
        Switch data_connection = (Switch) view.findViewById(R.id.data_connection);
        Switch phone_evdo_signal = (Switch) view.findViewById(R.id.phone_evdo_signal);
        Switch phone_signal = (Switch) view.findViewById(R.id.phone_signal);
        Switch otg_mouse = (Switch) view.findViewById(R.id.otg_mouse);
        Switch otg_keyboard = (Switch) view.findViewById(R.id.otg_keyboard);
        Switch felica_lock = (Switch) view.findViewById(R.id.felica_lock);
        Switch answering_memo = (Switch) view.findViewById(R.id.answering_memo);
        Switch ime = (Switch) view.findViewById(R.id.ime);
        Switch sync_failing = (Switch) view.findViewById(R.id.sync_failing);
        Switch sync_active = (Switch) view.findViewById(R.id.sync_active);
        Switch nfclock = (Switch) view.findViewById(R.id.nfclock);
        Switch secure = (Switch) view.findViewById(R.id.secure);
        Switch power_saver = (Switch) view.findViewById(R.id.power_saver);

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

        switches.add(knox_container);
        switches.add(smart_network);
        switches.add(glove);
        switches.add(gesture);
        switches.add(smart_scroll);
        switches.add(face);
        switches.add(gps);
        switches.add(lbs);
        switches.add(wearable_gear);
        switches.add(femtoicon);
        switches.add(rcs);
        switches.add(wifi_p2p);
        switches.add(wifi_ap);
        switches.add(wifi_oxygen);
        switches.add(phone_signal_second_stub);
        switches.add(toddler);
        switches.add(ims_volte);
        switches.add(keyguard_wakeup);
        switches.add(safezone);
        switches.add(wimax);
        switches.add(smart_bonding);
        switches.add(private_mode);

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
        String SLOT_ALARM = "alarm,alarm_clock";
        String SLOT_HOTSPOT = "hotspot";
        String SLOT_DATA_SAVER = "data_saver";
        String SLOT_NFC = "nfc,nfc_on";
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

        String SLOT_REMOTE_CALL = "remote_call";
        String SLOT_OTG_MOUSE = "otg_mouse";
        String SLOT_OTG_KEYBOARD = "otg_keyboard";
        String SLOT_DMB = "dmb";
        String SLOT_FELICA_LOCK = "felica_lock";
        String SLOT_ANSWERING_MEMO = "answering_memo";
        String SLOT_IME = "ime";
        String SLOT_SYNC_FAILING = "sync_failing";
        String SLOT_SYNC_ACTIVE = "sync_active";
        String SLOT_NFCLOCK = "nfclock";
        String SLOT_TTY = "tty";
        String SLOT_WIFI_CALLING = "wifi_calling";
        String SLOT_CDMA_ERI = "cdma_eri";
        String SLOT_DATA_CONNECTION = "data_connection";
        String SLOT_PHONE_EVDO_SIGNAL = "phone_evdo_signal";
        String SLOT_PHONE_SIGNAL = "phone_signal";
        String SLOT_SECURE = "secure";
        String SLOT_VOLTE = "volte";

        String SLOT_VOWIFI = "vowifi";
        String SLOT_POWER_SAVER = "power_saver";

        String SLOT_KNOX_CONTAINER = "knox_container";
        String SLOT_SMART_NETWORK = "smart_network";
        String SLOT_GLOVE = "glove";
        String SLOT_GESTURE = "gesture";
        String SLOT_SMART_SCROLL = "smart_scroll";
        String SLOT_FACE = "face";
        String SLOT_GPS = "gps";
        String SLOT_LBS = "lbs";
        String SLOT_WEARABLE_GEAR = "wearable_gear";
        String SLOT_FEMTOICON = "femtoicon";
        String SLOT_RCS = "com.samsung.rcs";
        String SLOT_WIFI_P2P = "wifi_p2p";
        String SLOT_WIFI_AP = "wifi_ap";
        String SLOT_WIFI_OXYGEN = "wifi_oxygen";
        String SLOT_PHONE_SIGNAL_SECOND_STUB = "phone_signal_second_stub";
        String SLOT_TODDLER = "toddler";
        String SLOT_IMS_VOLTE = "ims_volte";
        String SLOT_KEYGUARD_WAKEUP = "keyguard_wakeup";
        String SLOT_SAFEZONE = "safezone";
        String SLOT_WIMAX = "wimax";
        String SLOT_SMART_BONDING = "smart_bonding";
        String SLOT_PRIVATE_MODE = "private_mode";

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

        activity.setThings.switches(knox_container, SLOT_KNOX_CONTAINER, ICON_BLACKLIST, view);
        activity.setThings.switches(smart_network, SLOT_SMART_NETWORK, ICON_BLACKLIST, view);
        activity.setThings.switches(glove, SLOT_GLOVE, ICON_BLACKLIST, view);
        activity.setThings.switches(gesture, SLOT_GESTURE, ICON_BLACKLIST, view);
        activity.setThings.switches(smart_scroll, SLOT_SMART_SCROLL, ICON_BLACKLIST, view);
        activity.setThings.switches(face, SLOT_FACE, ICON_BLACKLIST, view);
        activity.setThings.switches(gps, SLOT_GPS, ICON_BLACKLIST, view);
        activity.setThings.switches(lbs, SLOT_LBS, ICON_BLACKLIST, view);
        activity.setThings.switches(wearable_gear, SLOT_WEARABLE_GEAR, ICON_BLACKLIST, view);
        activity.setThings.switches(femtoicon, SLOT_FEMTOICON, ICON_BLACKLIST, view);
        activity.setThings.switches(rcs, SLOT_RCS, ICON_BLACKLIST, view);
        activity.setThings.switches(wifi_p2p, SLOT_WIFI_P2P, ICON_BLACKLIST, view);
        activity.setThings.switches(wifi_ap, SLOT_WIFI_AP, ICON_BLACKLIST, view);
        activity.setThings.switches(wifi_oxygen, SLOT_WIFI_OXYGEN, ICON_BLACKLIST, view);
        activity.setThings.switches(phone_signal_second_stub, SLOT_PHONE_SIGNAL_SECOND_STUB, ICON_BLACKLIST, view);
        activity.setThings.switches(toddler, SLOT_TODDLER, ICON_BLACKLIST, view);
        activity.setThings.switches(ims_volte, SLOT_IMS_VOLTE, ICON_BLACKLIST, view);
        activity.setThings.switches(keyguard_wakeup, SLOT_KEYGUARD_WAKEUP, ICON_BLACKLIST, view);
        activity.setThings.switches(safezone, SLOT_SAFEZONE, ICON_BLACKLIST, view);
        activity.setThings.switches(wimax, SLOT_WIMAX, ICON_BLACKLIST, view);
        activity.setThings.switches(smart_bonding, SLOT_SMART_BONDING, ICON_BLACKLIST, view);
        activity.setThings.switches(private_mode, SLOT_PRIVATE_MODE, ICON_BLACKLIST, view);

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
