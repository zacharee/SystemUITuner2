package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

import java.util.ArrayList;

/**
 * Created by Zacha on 4/5/2017.
 */

@SuppressWarnings("ALL")
public class TouchWiz extends Fragment {
    private MainActivity activity;
    private View view;

    private final String SECURE = "secure";
    private final String GLOBAL = "global";
    private final String ICON_BLACKLIST = "icon_blacklist";

    private final ArrayList<Switch> switches = new ArrayList<>();
    private BroadcastReceiver check_toggles_receiver;

    private final String SLOT_KNOX_CONTAINER = "knox_container";
    private final String SLOT_SMART_NETWORK = "smart_network";
    private final String SLOT_GLOVE = "glove";
    private final String SLOT_GESTURE = "gesture";
    private final String SLOT_SMART_SCROLL = "smart_scroll";
    private final String SLOT_FACE = "face";
    private final String SLOT_GPS = "gps";
    private final String SLOT_LBS = "lbs";
    private final String SLOT_WEARABLE_GEAR = "wearable_gear";
    private final String SLOT_FEMTOICON = "femtoicon";
    private final String SLOT_RCS = "com.samsung.rcs";
    private final String SLOT_WIFI_P2P = "wifi_p2p";
    private final String SLOT_WIFI_AP = "wifi_ap";
    private final String SLOT_WIFI_OXYGEN = "wifi_oxygen";
    private final String SLOT_PHONE_SIGNAL_SECOND_STUB = "phone_signal_second_stub";
    private final String SLOT_TODDLER = "toddler";
    private final String SLOT_IMS_VOLTE = "ims_volte";
    private final String SLOT_KEYGUARD_WAKEUP = "keyguard_wakeup";
    private final String SLOT_SAFEZONE = "safezone";
    private final String SLOT_WIMAX = "wimax";
    private final String SLOT_SMART_BONDING = "smart_bonding";
    private final String SLOT_PRIVATE_MODE = "private_mode";

    private final String RESET_BLACKLIST = "reset_blacklist";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity)getActivity();
        }

        view = inflater.inflate(R.layout.fragment_tw, container, false);

        setupSwitches();
        setupButtons();
        setupReceivers();

        return view;
    }

    @Override
    public void onDestroy() {
        activity.unregisterReceiver(check_toggles_receiver);
        super.onDestroy();
    }

    private void setupSwitches() {
        Switch high_brightness_warning = view.findViewById(R.id.high_brightness_warning);

        Switch knox_container = view.findViewById(R.id.knox_container);
        Switch smart_network = view.findViewById(R.id.smart_network);
        Switch glove = view.findViewById(R.id.glove);
        Switch gesture = view.findViewById(R.id.gesture);
        Switch smart_scroll = view.findViewById(R.id.smart_scroll);
        Switch face = view.findViewById(R.id.face);
        Switch gps = view.findViewById(R.id.gps);
        Switch lbs = view.findViewById(R.id.lbs);
        Switch wearable_gear = view.findViewById(R.id.wearable_gear);
        Switch femtoicon = view.findViewById(R.id.femtoicon);
        Switch rcs = view.findViewById(R.id.rcs);
        Switch wifi_p2p = view.findViewById(R.id.wifi_p2p);
        Switch wifi_ap = view.findViewById(R.id.wifi_ap);
        Switch wifi_oxygen = view.findViewById(R.id.wifi_oxygen);
        Switch phone_signal_second_stub = view.findViewById(R.id.phone_signal_second_stub);
        Switch toddler = view.findViewById(R.id.toddler);
        Switch ims_volte = view.findViewById(R.id.ims_volte);
        Switch keyguard_wakeup = view.findViewById(R.id.keyguard_wakeup);
        Switch safezone = view.findViewById(R.id.safezone);
        Switch wimax = view.findViewById(R.id.wimax);
        Switch smart_bonding = view.findViewById(R.id.smart_bonding);
        Switch private_mode = view.findViewById(R.id.private_mode);

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

        high_brightness_warning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.setThings.settings(GLOBAL, "limit_brightness_state", isChecked ? "80, 80" : null);
            }
        });

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
    }

    private void setupButtons() {
        Button reset_blacklist = view.findViewById(R.id.reset_blacklist);

        activity.setThings.buttons(reset_blacklist, RESET_BLACKLIST);
    }

    private void setupReceivers() {
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
    }

    private void checkBL(@SuppressWarnings("SameParameterValue") boolean tralse) {
        for (Switch toggle : switches) {
            toggle.setChecked(tralse);
        }
    }
}
