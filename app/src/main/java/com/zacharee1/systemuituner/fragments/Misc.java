package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 4/18/2017.
 */

public class Misc extends Fragment {
    private MainActivity activity;

    private Button animApply;
    private Button transApply;
    private Button winApply;
    private Button globalApply;
    private Button secureApply;
    private Button systemApply;

    private TextInputEditText anim;
    private TextInputEditText trans;
    private TextInputEditText win;
    private TextInputEditText custom_global;
    private TextInputEditText custom_secure;
    private TextInputEditText custom_system;

    // --Commented out by Inspection (5/7/2017 8:00 AM):private final int alertRed = R.drawable.ic_warning_red;

    private String animScale;
    private String transScale;
    private String winScale;
    private String global;
    private String secure;
    private String system;

    private boolean mNightModeAuto;
    private boolean mNightModeOverride;
    private Switch night_mode_auto;
    private Switch night_mode_override;
    private Switch night_mode_adjust_tint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        boolean customSettingsEnabled = activity.setThings.sharedPreferences.getBoolean("customSettings", false);

        global = "";
        secure = "";
        system = "";

        View view = inflater.inflate(R.layout.fragment_misc, container, false);

        Switch show_full_zen = (Switch) view.findViewById(R.id.show_full_zen);
        Switch hu_notif = (Switch) view.findViewById(R.id.hu_notif);
        Switch vol_warn = (Switch) view.findViewById(R.id.vol_warn);
        Switch power_notifs = (Switch) view.findViewById(R.id.power_notifications);

        CardView custom_settings = (CardView) view.findViewById(R.id.custom_settings);
        custom_settings.setVisibility(customSettingsEnabled ? View.VISIBLE : View.GONE);

        animApply = (Button) view.findViewById(R.id.apply_anim);
        transApply = (Button) view.findViewById(R.id.apply_trans);
        winApply = (Button) view.findViewById(R.id.apply_win);
        globalApply = (Button) view.findViewById(R.id.apply_global);
        secureApply = (Button) view.findViewById(R.id.apply_secure);
        systemApply = (Button) view.findViewById(R.id.apply_system);

        Switch clock_seconds = (Switch) view.findViewById(R.id.clock_seconds);
        Switch battery_percent = (Switch) view.findViewById(R.id.battery_percent);

        //custom switch text
        //noinspection deprecation
        battery_percent.setText(Html.fromHtml(getResources().getText(R.string.battery_percentage) + "<br /><small> <font color=\"#777777\">" + getResources().getText(R.string.reboot_required) + "</font></small>"));

        CardView power_notif_controls = (CardView) view.findViewById(R.id.power_notification_controls_card);

        if (Build.VERSION.SDK_INT > 23) {
            clock_seconds.setVisibility(View.VISIBLE); //only show switch if user is on Nougat or later
            power_notif_controls.setVisibility(View.VISIBLE); //this is a Nougat feature; only show it on Nougat devices
        } else {
            clock_seconds.setVisibility(View.GONE);
            power_notif_controls.setVisibility(View.GONE);
        }

        anim = (TextInputEditText) view.findViewById(R.id.anim_text);
        trans = (TextInputEditText) view.findViewById(R.id.trans_text);
        win = (TextInputEditText) view.findViewById(R.id.win_text);
        custom_global = (TextInputEditText) view.findViewById(R.id.global_settings);
        custom_secure = (TextInputEditText) view.findViewById(R.id.secure_settings);
        custom_system = (TextInputEditText) view.findViewById(R.id.system_settings);

        animScale = Settings.Global.getString(activity.getContentResolver(), "animator_duration_scale");
        if (animScale == null) animScale = "1.0";
        transScale = Settings.Global.getString(activity.getContentResolver(), "transition_animation_scale");
        if (transScale == null) transScale = "1.0";
        winScale = Settings.Global.getString(activity.getContentResolver(), "window_animation_scale");
        if (winScale == null) winScale = "1.0";

        anim.setHint(getResources().getText(R.string.animator_duration_scale) + " (" + String.valueOf(animScale) + ")");
        trans.setHint(getResources().getText(R.string.transition_animation_scale) + " (" + String.valueOf(transScale) + ")");
        win.setHint(getResources().getText(R.string.window_animation_scale) + " (" + String.valueOf(winScale) + ")");
        custom_global.setHint(getResources().getText(R.string.global));
        custom_secure.setHint(getResources().getText(R.string.secure));
        custom_system.setHint(getResources().getText(R.string.system));

        activity.setThings.switches(show_full_zen, "sysui_show_full_zen", "secure", view); //switch listener
        activity.setThings.switches(hu_notif, "heads_up_notifications_enabled", "global", view);
        activity.setThings.switches(vol_warn, "audio_safe_volume_state", "global", view);

        activity.setThings.switches(clock_seconds, "clock_seconds", "secure", view);
        activity.setThings.switches(battery_percent, "status_bar_show_battery_percent", "system", view);

        activity.setThings.switches(power_notifs, "show_importance_slider", "secure", view);

        buttons(animApply);
        buttons(transApply);
        buttons(winApply);
        buttons(globalApply);
        buttons(secureApply);
        buttons(systemApply);

        textFields(anim);
        textFields(trans);
        textFields(win);
        textFields(custom_global);
        textFields(custom_secure);
        textFields(custom_system);

        if (activity.setThings.SDK_INT > 23 && activity.setThings.SDK_INT < 25) {

            night_mode_auto = (Switch) view.findViewById(R.id.night_mode_auto);
            night_mode_override = (Switch) view.findViewById(R.id.night_mode_override);
            night_mode_adjust_tint = (Switch) view.findViewById(R.id.night_mode_adjust_tint);

            night_mode_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mNightModeAuto = isChecked;
                    setNightMode();
                }
            });

            night_mode_override.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mNightModeOverride = isChecked;
                    setNightMode();
                }
            });

            night_mode_adjust_tint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    activity.setThings.settings("secure", "tuner_night_mode_adjust_tint", isChecked ? 1 + "" : 0 + "");
                }
            });

            night_mode_adjust_tint.setChecked(Settings.Secure.getInt(activity.getContentResolver(), "tuner_night_mode_adjust_tint", 0) == 1);

            getNightMode();
        } else {
            CardView night_mode_card = (CardView) view.findViewById(R.id.night_mode_card);
            night_mode_card.setVisibility(View.GONE);
        }

        return view;
    }

    private void getNightMode() {
        int val = Settings.Secure.getInt(activity.getContentResolver(), "twilight_mode", 0);

        switch (val) {
            case 0:
                mNightModeAuto = false;
                mNightModeOverride = false;
                night_mode_auto.setChecked(false);
                night_mode_override.setChecked(false);
                break;
            case 1:
                mNightModeAuto = false;
                mNightModeOverride = true;
                night_mode_auto.setChecked(false);
                night_mode_override.setChecked(true);
                break;
            case 2:
                mNightModeAuto = true;
                mNightModeOverride = false;
                night_mode_auto.setChecked(true);
                night_mode_override.setChecked(false);
                break;
            case 4:
                mNightModeAuto = true;
                mNightModeOverride = true;
                night_mode_auto.setChecked(true);
                night_mode_override.setChecked(true);
                break;
        }
    }

    private void setNightMode() {
        int val;

        if (mNightModeOverride && !mNightModeAuto) val = 1;
        else if (!mNightModeOverride && mNightModeAuto) val = 2;
        else if (mNightModeOverride) val = 4; //implied "&& mNightModeAuto == true"
        else val = 0;

        activity.setThings.settings("secure", "twilight_mode", val + "");
    }

    private void textFields(final TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textInputEditText.getText().length() > 0) {
                    final String value = textInputEditText.getText().toString();

                    if (textInputEditText == anim) animScale = value;
                    else if (textInputEditText == trans) transScale = value;
                    else if (textInputEditText == win) winScale = value;
                    else if (textInputEditText == custom_global) global = value;
                    else if (textInputEditText == custom_secure) secure = value;
                    else if (textInputEditText == custom_system) system = value;
                }
            }
        });
    }

    private void buttons(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pref;
                String val;
                String type;
                String[] parsedString;

                if (button == animApply) {
                    pref = "animator_duration_scale";
                    if (animScale.contains(".") && !animScale.contains("0.")) animScale = "0" + animScale;
                    if (animScale.indexOf(".") == animScale.length() - 1) animScale = animScale + "0";
                    if (Float.valueOf(animScale) > 10) animScale = "10";
                    val = animScale;
                    type = "global";
                } else if (button == transApply) {
                    pref = "transition_animation_scale";
                    if (transScale.contains(".") && !transScale.contains("0.")) transScale = "0" + transScale;
                    if (transScale.indexOf(".") == transScale.length() - 1) transScale = transScale + "0";
                    if (Float.valueOf(transScale) > 10) transScale = "10";
                    val = transScale;
                    type = "global";
                } else if (button == winApply) {
                    pref = "window_animation_scale";
                    if (winScale.contains(".") && !winScale.contains("0.")) winScale = "0" + winScale;
                    if (winScale.indexOf(".") == winScale.length() - 1) winScale = winScale + "0";
                    if (Float.valueOf(winScale) > 10) winScale = "10";
                    val = winScale;
                    type = "global";
                } else if (button == globalApply) {
                    parsedString = global.split("[ ]");
                    pref = parsedString[0];
                    if (parsedString.length > 1) val = parsedString[1];
                    else val = "";
                    type = "global";
                } else if (button == secureApply) {
                    parsedString = secure.split("[ ]");
                    pref = parsedString[0];
                    if (parsedString.length > 1) val = parsedString[1];
                    else val = "";
                    type = "secure";
                } else if (button == systemApply) {
                    parsedString = system.split("[ ]");
                    pref = parsedString[0];
                    if (parsedString.length > 1) val = parsedString[1];
                    else val = "";
                    type = "system";
                } else {
                    pref = "";
                    val = "";
                    type = "";
                }

                activity.setThings.settings(type, pref, val);
            }
        });
    }
}
