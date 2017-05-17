package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PagerSnapHelper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.widget.Toast;

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
    private boolean mNightDisplayAuto;
    private boolean mNightDisplayActive;
    private boolean mNightDisplayCustom;
    private Button set_start;
    private Button set_end;
    private LinearLayout custom_time;
    private long startTime;
    private long startHour;
    private long startMinute;
    private String start;
    private String end;
    private long endTime;
    private long endHour;
    private long endMinute;
    private View view;
    private Switch show_full_zen;
    private Switch hu_notif;
    private Switch vol_warn;
    private Switch power_notifs;
    private Switch clock_seconds;
    private Switch battery_percent;
    private CardView power_notif_controls;

    String SHOW_FULL_ZEN = "sysui_show_full_zen";
    String HUN_ENABLED = "heads_up_notifications_enabled";
    String SAFE_AUDIO = "audio_safe_volume_state";
    String CLOCK_SECONDS = "clock_seconds";
    String BATTERY_PERCENT = "status_bar_show_battery_percent";
    String POW_NOTIFS = "show_importance_slider";
    String NIGHT_MODE_TINT = "tuner_night_mode_adjust_tint";
    String TWILIGHT_MODE = "twilight_mode";

    String SECURE = "secure";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        global = "";
        secure = "";
        system = "";

        view = inflater.inflate(R.layout.fragment_misc, container, false);

        setupCustomSettings();
        setupSwitches();
        setupScales();
        setupSettings();
        chooseNightType();

        return view;
    }

    private void chooseNightType() {
        if (activity.setThings.SDK_INT == 24) {
            CardView night_display_card = (CardView) view.findViewById(R.id.night_display_card);
            night_display_card.setVisibility(View.GONE);
            setupNightMode();
        } else if (activity.setThings.SDK_INT > 24) {
            CardView night_mode_card = (CardView) view.findViewById(R.id.night_mode_card);
            night_mode_card.setVisibility(View.GONE);
            setupNightDisplay();
        } else {
            CardView night_mode_card = (CardView) view.findViewById(R.id.night_mode_card);
            night_mode_card.setVisibility(View.GONE);
            CardView night_display_card = (CardView) view.findViewById(R.id.night_display_card);
            night_display_card.setVisibility(View.GONE);
        }
    }

    private void setupCustomSettings() {
        boolean customSettingsEnabled = activity.setThings.sharedPreferences.getBoolean("customSettings", false);
        final CardView custom_settings = (CardView) view.findViewById(R.id.custom_settings);
        custom_settings.setVisibility(customSettingsEnabled ? View.VISIBLE : View.GONE);
    }

    private void setupSwitches() {
        show_full_zen = (Switch) view.findViewById(R.id.show_full_zen);
        hu_notif = (Switch) view.findViewById(R.id.hu_notif);
        vol_warn = (Switch) view.findViewById(R.id.vol_warn);
        power_notifs = (Switch) view.findViewById(R.id.power_notifications);
        clock_seconds = (Switch) view.findViewById(R.id.clock_seconds);
        battery_percent = (Switch) view.findViewById(R.id.battery_percent);
        power_notif_controls = (CardView) view.findViewById(R.id.power_notification_controls_card);

        battery_percent.setText(Html.fromHtml(getResources().getText(R.string.battery_percentage) + "<br /><small> <font color=\"#777777\">" + getResources().getText(R.string.reboot_required) + "</font></small>"));

        if (Build.VERSION.SDK_INT > 23) {
            clock_seconds.setVisibility(View.VISIBLE); //only show switch if user is on Nougat or later
            power_notif_controls.setVisibility(View.VISIBLE); //this is a Nougat feature; only show it on Nougat devices
        } else {
            clock_seconds.setVisibility(View.GONE);
            power_notif_controls.setVisibility(View.GONE);
        }

        activity.setThings.switches(show_full_zen, SHOW_FULL_ZEN, "secure", view); //switch listener
        activity.setThings.switches(hu_notif, HUN_ENABLED, "global", view);
        activity.setThings.switches(vol_warn, SAFE_AUDIO, "global", view);

        activity.setThings.switches(clock_seconds, CLOCK_SECONDS, "secure", view);
        activity.setThings.switches(battery_percent, BATTERY_PERCENT, "system", view);

        activity.setThings.switches(power_notifs, POW_NOTIFS, "secure", view);
    }

    private void setupScales() {
        animApply = (Button) view.findViewById(R.id.apply_anim);
        transApply = (Button) view.findViewById(R.id.apply_trans);
        winApply = (Button) view.findViewById(R.id.apply_win);

        anim = (TextInputEditText) view.findViewById(R.id.anim_text);
        trans = (TextInputEditText) view.findViewById(R.id.trans_text);
        win = (TextInputEditText) view.findViewById(R.id.win_text);

        animScale = Settings.Global.getString(activity.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE);
        if (animScale == null) animScale = "1.0";
        transScale = Settings.Global.getString(activity.getContentResolver(), Settings.Global.TRANSITION_ANIMATION_SCALE);
        if (transScale == null) transScale = "1.0";
        winScale = Settings.Global.getString(activity.getContentResolver(), Settings.Global.WINDOW_ANIMATION_SCALE);
        if (winScale == null) winScale = "1.0";

        anim.setHint(getResources().getText(R.string.animator_duration_scale) + " (" + String.valueOf(animScale) + ")");
        trans.setHint(getResources().getText(R.string.transition_animation_scale) + " (" + String.valueOf(transScale) + ")");
        win.setHint(getResources().getText(R.string.window_animation_scale) + " (" + String.valueOf(winScale) + ")");

        buttons(animApply);
        buttons(transApply);
        buttons(winApply);

        textFields(anim);
        textFields(trans);
        textFields(win);
    }

    private void setupSettings() {
        globalApply = (Button) view.findViewById(R.id.apply_global);
        secureApply = (Button) view.findViewById(R.id.apply_secure);
        systemApply = (Button) view.findViewById(R.id.apply_system);

        custom_global = (TextInputEditText) view.findViewById(R.id.global_settings);
        custom_secure = (TextInputEditText) view.findViewById(R.id.secure_settings);
        custom_system = (TextInputEditText) view.findViewById(R.id.system_settings);

        custom_global.setHint(getResources().getText(R.string.global));
        custom_secure.setHint(getResources().getText(R.string.secure));
        custom_system.setHint(getResources().getText(R.string.system));

        buttons(globalApply);
        buttons(secureApply);
        buttons(systemApply);

        textFields(custom_global);
        textFields(custom_secure);
        textFields(custom_system);
    }

    private void setupNightMode() {
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
                activity.setThings.settings(SECURE, NIGHT_MODE_TINT, isChecked ? 1 + "" : 0 + "");
            }
        });

        night_mode_adjust_tint.setChecked(Settings.Secure.getInt(activity.getContentResolver(), NIGHT_MODE_TINT, 0) == 1);

        getNightMode();
    }

    private void setupNightDisplay() {
        set_start = (Button) view.findViewById(R.id.set_start_time);
        set_end = (Button) view.findViewById(R.id.set_end_time);
        custom_time = (LinearLayout) view.findViewById(R.id.custom_time_layout);

        setTimes();

        Switch night_display_auto = (Switch) view.findViewById(R.id.night_display_auto);
        Switch night_display_active = (Switch) view.findViewById(R.id.night_display_activated);
        Switch night_display_custom = (Switch) view.findViewById(R.id.night_display_custom_times);

        night_display_auto.setChecked(Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_AUTO_MODE, 0) == 1);
        night_display_active.setChecked(Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_ACTIVATED, 0) == 1);

        try {
            night_display_custom.setChecked(Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME).length() > 0 ||
                    Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME).length() > 0);
            custom_time.setVisibility(night_display_custom.isChecked() ? View.VISIBLE : View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        night_display_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNightDisplayAuto = isChecked;
                Settings.Secure.putInt(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_AUTO_MODE, isChecked ? 1 : 0);
            }
        });

        night_display_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNightDisplayActive = isChecked;
                Settings.Secure.putInt(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_ACTIVATED, isChecked ? 1 : 0);
            }
        });

        night_display_custom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                mNightDisplayCustom = isChecked;

                custom_time.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                if (!isChecked) {
                    activity.setThings.editor.putString(Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME, Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME));
                    activity.setThings.editor.putString(Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME, Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME));
                    activity.setThings.editor.apply();
                    activity.setThings.settings(SECURE, Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME, "");
                    activity.setThings.settings(SECURE, Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME, "");
                } else {
                    activity.setThings.settings(SECURE, Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME, activity.setThings.sharedPreferences.getString(Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME, "0"));
                    activity.setThings.settings(SECURE, Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME, activity.setThings.sharedPreferences.getString(Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME, "0"));
                    setTimes();
                }
            }
        });

        set_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimes();

                TimePickerDialog customStart = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute2) {
                        long hrmil = TimeUnit.HOURS.toMillis(hourOfDay);
                        long minmil = TimeUnit.MINUTES.toMillis(minute2);

                        long time = hrmil + minmil;

                        activity.setThings.settings(SECURE, Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME, time + "");
                        set_start.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute2));
                    }
                }, (int)startHour, (int)startMinute, true);
                customStart.setTitle(getResources().getText(R.string.custom_start_title));
                Toast.makeText(activity.getApplicationContext(), getResources().getText(R.string.custom_start_title), Toast.LENGTH_SHORT).show();
                customStart.show();
            }
        });

        set_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimes();

                TimePickerDialog customEnd = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute3) {
                        long hrmil = TimeUnit.HOURS.toMillis(hourOfDay);
                        long minmil = TimeUnit.MINUTES.toMillis(minute3);

                        long time = hrmil + minmil;

                        activity.setThings.settings(SECURE, Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME, time + "");
                        set_end.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute3));
                    }
                }, (int)endHour, (int)endMinute, true);
                customEnd.setTitle(getResources().getText(R.string.custom_end_title));
                Toast.makeText(activity.getApplicationContext(), getResources().getText(R.string.custom_end_title), Toast.LENGTH_SHORT).show();
                customEnd.show();
            }
        });
    }

    private void getNightMode() {
        int val = Settings.Secure.getInt(activity.getContentResolver(), TWILIGHT_MODE, 0);

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

        activity.setThings.settings(SECURE, TWILIGHT_MODE, val + "");
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
                    pref = Settings.Global.ANIMATOR_DURATION_SCALE;
                    if (animScale.contains(".") && !animScale.contains("0.")) animScale = "0" + animScale;
                    if (animScale.indexOf(".") == animScale.length() - 1) animScale = animScale + "0";
                    if (Float.valueOf(animScale) > 10) animScale = "10";
                    val = animScale;
                    type = "global";
                } else if (button == transApply) {
                    pref = Settings.Global.TRANSITION_ANIMATION_SCALE;
                    if (transScale.contains(".") && !transScale.contains("0.")) transScale = "0" + transScale;
                    if (transScale.indexOf(".") == transScale.length() - 1) transScale = transScale + "0";
                    if (Float.valueOf(transScale) > 10) transScale = "10";
                    val = transScale;
                    type = "global";
                } else if (button == winApply) {
                    pref = Settings.Global.WINDOW_ANIMATION_SCALE;
                    if (winScale.contains(".") && !winScale.contains("0.")) winScale = "0" + winScale;
                    if (winScale.indexOf(".") == winScale.length() - 1) winScale = winScale + "0";
                    if (Float.valueOf(winScale) > 10) winScale = "10";
                    val = winScale;
                    type = "global";
                } else if (button == globalApply) {
                    parsedString = global.split("[ ]");
                    if (parsedString.length > 0) pref = parsedString[0];
                    else pref = "";
                    if (parsedString.length > 1) val = parsedString[1];
                    else val = "";
                    type = "global";
                } else if (button == secureApply) {
                    parsedString = secure.split("[ ]");
                    if (parsedString.length > 0) pref = parsedString[0];
                    else pref = "";
                    if (parsedString.length > 1) val = parsedString[1];
                    else val = "";
                    type = "secure";
                } else if (button == systemApply) {
                    parsedString = system.split("[ ]");
                    if (parsedString.length > 0) pref = parsedString[0];
                    else pref = "";
                    if (parsedString.length > 1) val = parsedString[1];
                    else val = "";
                    type = "system";
                } else {
                    pref = "";
                    val = "";
                    type = "";
                }

                activity.setThings.settings(type, pref, val);
                Toast.makeText(activity.getApplicationContext(), getResources().getText(R.string.applied), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTimes() {
        start = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME);
        if (start == null || start.length() < 1) start = "0";
        startTime = Long.decode(start);
        startHour = TimeUnit.MILLISECONDS.toHours(startTime);
        startMinute = TimeUnit.MILLISECONDS.toMinutes(startTime) % TimeUnit.HOURS.toMinutes(1);

        end = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME);
        if (end == null || end.length() < 1) end = "0";
        endTime = Long.decode(end);
        endHour = TimeUnit.MILLISECONDS.toHours(endTime);
        endMinute = TimeUnit.MILLISECONDS.toMinutes(endTime) % TimeUnit.HOURS.toMinutes(1);

        set_start.setText(String.format(Locale.ENGLISH, "%02d:%02d", startHour, startMinute));
        set_end.setText(String.format(Locale.ENGLISH, "%02d:%02d", endHour, endMinute));
    }
}
