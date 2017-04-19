package com.zacharee1.systemuituner;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Zacha on 4/5/2017.
 */

public class DemoFragment extends Fragment {
    public View view;
    public MainActivity activity;

    public Switch showDemo;
    public Switch batteryPluggedSwitch;
    public Switch showNotifSwitch;
    public Switch airplaneMode;

    public Button selectTime;
    public Button selectBatteryLevel;
    public Button enableDemo;

    public Spinner wifi;
    public Spinner mobile;
    public Spinner mobileTypeSpinner;
    public Spinner statStyleSpinner;

    public int hour = 12;
    public int minute = 00;
    public int mobileLevel = 3;
    public int wifiLevel = 3;
    public int batteryLevel = 50;

    public String showMobile = "show";
    public String showWiFi = "show";
    public String showAirplane = "hide";
    public String mobileType = "lte";
    public String batteryPlugged = "false";
    public String showNotifs = "false";
    public String statBarStyle = "opaque";

    DemoFragment demo;

    boolean isRooted;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("Demo Mode");

        view = inflater.inflate(R.layout.fragment_demo, container, false);

        demo = new DemoFragment();

        isRooted = activity.sharedPreferences.getBoolean("isRooted", false);

        wifi = (Spinner) view.findViewById(R.id.wifi_strength);
        mobile = (Spinner) view.findViewById(R.id.mobile_strength);
        mobileTypeSpinner = (Spinner) view.findViewById(R.id.mobile_type);
        statStyleSpinner = (Spinner) view.findViewById(R.id.stat_bar_style);

        showDemo = (Switch) view.findViewById(R.id.show_demo);
        batteryPluggedSwitch = (Switch) view.findViewById(R.id.battery_plugged);
        airplaneMode = (Switch) view.findViewById(R.id.show_airplane);
        showNotifSwitch = (Switch) view.findViewById(R.id.show_notifs);

        selectTime = (Button) view.findViewById(R.id.select_time);
        enableDemo = (Button) view.findViewById(R.id.enable_demo);
        selectBatteryLevel = (Button) view.findViewById(R.id.select_battery_level_button);

        TextView title = (TextView) view.findViewById(R.id.title_demo);

        if (activity.sharedPreferences.getBoolean("isDark", false)) {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        setSpinnerAdapters(wifi, R.array.wifi_strength);
        setSpinnerAdapters(mobile, R.array.mobile_strength);
        setSpinnerAdapters(mobileTypeSpinner, R.array.mobile_type);
        setSpinnerAdapters(statStyleSpinner, R.array.stat_bar_style);

        wifi.setSelection(activity.sharedPreferences.getInt("wifiLevel", 3));
        mobile.setSelection(activity.sharedPreferences.getInt("mobileLevel", 3));
        mobileTypeSpinner.setSelection(activity.sharedPreferences.getInt("mobileType1", 0));
        statStyleSpinner.setSelection(activity.sharedPreferences.getInt("statBarStyle1", 0));

        statBarStyle = (String)statStyleSpinner.getItemAtPosition(activity.sharedPreferences.getInt("statBarStyle1", 0));

        showDemo.setChecked(activity.sharedPreferences.getBoolean("demoOn", false));
        batteryPluggedSwitch.setChecked(activity.sharedPreferences.getBoolean("isCharging", false));
        if (activity.sharedPreferences.getBoolean("isCharging", false)) {
            batteryPlugged = "true";
        }
        airplaneMode.setChecked(activity.sharedPreferences.getBoolean("showAirplane", false));
        if (activity.sharedPreferences.getBoolean("showAirplane", false)) {
            showAirplane = "show";
        }
        showNotifSwitch.setChecked(activity.sharedPreferences.getBoolean("showNotifs", false));
        if (activity.sharedPreferences.getBoolean("showNotifs", false)) {
            showNotifs = "true";
        }

        mobileType = (String)mobileTypeSpinner.getItemAtPosition(activity.sharedPreferences.getInt("mobileType1", 0));

        hour = activity.sharedPreferences.getInt("hour", 12);
        minute = activity.sharedPreferences.getInt("minute", 00);
        mobileLevel = activity.sharedPreferences.getInt("mobileLevel", 3);
        wifiLevel = activity.sharedPreferences.getInt("wifiLevel", 3);
        batteryLevel = activity.sharedPreferences.getInt("batteryLevel", 50);

        enableDemo(enableDemo);
        showDemo(showDemo);
        selectTime(selectTime);
        selectSignal(R.id.wifi_strength, wifi);
        selectSignal(R.id.mobile_strength, mobile);
        selectBatteryLevel(selectBatteryLevel);
        selectMobileType(mobileTypeSpinner);
        setBatteryPlugged(batteryPluggedSwitch);
        setShowNotifs(showNotifSwitch);
        setStatBarStyle(statStyleSpinner);
        setAirplaneMode(airplaneMode);
        return view;
    }

//    public void enableDemo(final Switch toggle) {
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            sudo("am broadcast -a com.android.systemui.demo --es command enter");
//                        }
//                    }).start();
//                } else {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            sudo("am broadcast -a com.android.systemui.demo --es command exit");
//                        }
//                    }).start();
//                }
//            }
//        });
//    }

    public void enableDemo(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.Global.putInt(activity.getContentResolver(), "sysui_demo_allowed", 1);
            }
        });
    }

    public void setSpinnerAdapters(Spinner spinner, int arrayID) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity.getApplicationContext(),
                arrayID, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setStatBarStyle(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activity.editor.putInt("statBarStyle1", position);
                activity.editor.apply();
                statBarStyle = String.valueOf(spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setShowNotifs(Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.editor.putBoolean("showNotifs", true);
                    showNotifs = "true";
                } else {
                    activity.editor.putBoolean("showNotifs", false);
                    showNotifs = "false";
                }
                activity.editor.apply();
            }
        });
    }

    public void setBatteryPlugged(Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.editor.putBoolean("isCharging", true);
                    batteryPlugged = "true";
                } else {
                    activity.editor.putBoolean("isCharging", false);
                    batteryPlugged = "false";
                }
                activity.editor.apply();
            }
        });
    }

    public void setAirplaneMode(Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.editor.putBoolean("showAirplane", true);
                    showAirplane = "show";
                } else {
                    activity.editor.putBoolean("showAirplane", false);
                    showAirplane = "hide";
                }
                activity.editor.apply();
            }
        });
    }

    public void selectMobileType(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = String.valueOf(spinner.getItemAtPosition(position));
                activity.editor.putInt("mobileType1", position);
                activity.editor.apply();
                mobileType = value;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void selectBatteryLevel(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(button.getContext());
                d.setTitle("NumberPicker");
                d.setContentView(R.layout.battery_level_dialog);
                Button b1 = (Button) d.findViewById(R.id.button_cancel);
                Button b2 = (Button) d.findViewById(R.id.button_set);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.select_battery_level);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setValue(activity.sharedPreferences.getInt("batteryLevel", 50));
                np.setWrapSelectorWheel(false);
                np.setSelected(false);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    }
                });
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
//                        tv.setText(String.valueOf(np.getValue()));
                        d.dismiss();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        activity.editor.putInt("batteryLevel", np.getValue());
                        activity.editor.apply();
                        batteryLevel = np.getValue();
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
    }

    public void selectTime(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;

                timePickerDialog = new TimePickerDialog(button.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        demo.hour = hourOfDay;
                        activity.editor.putInt("hour", hourOfDay);
                        demo.minute = minute;
                        activity.editor.putInt("minute", minute);
                        activity.editor.apply();
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Choose Time to Display");
                timePickerDialog.show();
            }
        });
    }

    public void selectSignal(final int signalID, final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (signalID) {
                    case R.id.wifi_strength:
                        wifiLevel = Integer.decode(String.valueOf(spinner.getItemAtPosition(position)));
                        activity.editor.putInt("wifiLevel", wifiLevel);
                        break;
                    case R.id.mobile_strength:
                        mobileLevel = Integer.decode(String.valueOf(spinner.getItemAtPosition(position)));
                        activity.editor.putInt("mobileLevel", mobileLevel);
                }
                activity.editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void showDemo(final Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            try {
                                Intent intent = new Intent("com.android.systemui.demo");
                                intent.putExtra("command", "enter");
                                getActivity().sendBroadcast(intent);

                                intent.putExtra("command", "clock");
                                intent.putExtra("hhmm", String.valueOf(demo.hour) + String.valueOf(demo.minute));
                                getActivity().sendBroadcast(intent);

                                intent.putExtra("command", "network");
                                intent.putExtra("mobile", String.valueOf(showMobile));
                                intent.putExtra("fully", "true");
                                intent.putExtra("level", String.valueOf(mobileLevel));
                                intent.putExtra("datatype", String.valueOf(mobileType));
                                getActivity().sendBroadcast(intent);

                                intent.removeExtra("mobile");
                                intent.removeExtra("datatype");
                                intent.putExtra("wifi", String.valueOf(showWiFi));
                                intent.putExtra("fully", "true");
                                intent.putExtra("level", String.valueOf(wifiLevel));
                                getActivity().sendBroadcast(intent);

                                intent.putExtra("airplane", String.valueOf(showAirplane));
                                getActivity().sendBroadcast(intent);

                                intent.putExtra("command", "battery");
                                intent.putExtra("level", String.valueOf(batteryLevel));
                                intent.putExtra("plugged", String.valueOf(batteryPlugged));
                                getActivity().sendBroadcast(intent);

                                intent.putExtra("command", "notifications");
                                intent.putExtra("visible", showNotifs);
                                getActivity().sendBroadcast(intent);

                                intent.putExtra("command", "bars");
                                intent.putExtra("mode", statBarStyle);
                                getActivity().sendBroadcast(intent);

                                activity.editor.putBoolean("demoOn", true);
                                activity.editor.apply();
                            } catch (Exception e) {
                                Log.e("Demo", e.getMessage());
                            }
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent("com.android.systemui.demo");
                            intent.putExtra("command", "exit");
                            getActivity().sendBroadcast(intent);
                            activity.editor.putBoolean("demoOn", false);
                            activity.editor.apply();
                        }
                    }).start();
                }
            }
        });
    }
}
