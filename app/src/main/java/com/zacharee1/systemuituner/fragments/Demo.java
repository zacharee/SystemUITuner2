package com.zacharee1.systemuituner.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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
import android.widget.TimePicker;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

import java.util.Calendar;

/**
 * Created by Zacha on 4/5/2017.
 */

@SuppressWarnings("ALL")
public class Demo extends Fragment {
    private MainActivity activity;

    private int hour = 12;
    private int minute = 00;
    private int mobileLevel = 3;
    private int wifiLevel = 3;
    private int batteryLevel = 50;

    private final String showMobile = "show";
    private final String showWiFi = "show";
    private String showAirplane = "hide";
    private String mobileType = "lte";
    private String batteryPlugged = "false";
    private String showNotifs = "false";
    private String statBarStyle = "opaque";

    private Demo demo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        View view = inflater.inflate(R.layout.fragment_demo, container, false);

        demo = new Demo();

        //spinners
        Spinner wifi = (Spinner) view.findViewById(R.id.wifi_strength);
        Spinner mobile = (Spinner) view.findViewById(R.id.mobile_strength);
        Spinner mobileTypeSpinner = (Spinner) view.findViewById(R.id.mobile_type);
        Spinner statStyleSpinner = (Spinner) view.findViewById(R.id.stat_bar_style);

        //switches
        Switch showDemo = (Switch) view.findViewById(R.id.show_demo);
        Switch batteryPluggedSwitch = (Switch) view.findViewById(R.id.battery_plugged);
        Switch airplaneMode = (Switch) view.findViewById(R.id.show_airplane);
        Switch showNotifSwitch = (Switch) view.findViewById(R.id.show_notifs);

        //buttons
        Button selectTime = (Button) view.findViewById(R.id.select_time);
        Button enableDemo = (Button) view.findViewById(R.id.enable_demo);
        Button selectBatteryLevel = (Button) view.findViewById(R.id.select_battery_level_button);

        //set spinner adapters
        setSpinnerAdapters(wifi, R.array.wifi_strength);
        setSpinnerAdapters(mobile, R.array.mobile_strength);
        setSpinnerAdapters(mobileTypeSpinner, R.array.mobile_type);
        setSpinnerAdapters(statStyleSpinner, R.array.stat_bar_style);

        //set spinner selections from sharedPrefs
        wifi.setSelection(activity.setThings.sharedPreferences.getInt("wifiLevel", 3));
        mobile.setSelection(activity.setThings.sharedPreferences.getInt("mobileLevel", 3));
        mobileTypeSpinner.setSelection(activity.setThings.sharedPreferences.getInt("mobileType1", 0));
        statStyleSpinner.setSelection(activity.setThings.sharedPreferences.getInt("statBarStyle1", 0));

        //set proper values from sharedPrefs
        statBarStyle = (String) statStyleSpinner.getItemAtPosition(activity.setThings.sharedPreferences.getInt("statBarStyle1", 0));

        showDemo.setChecked(activity.setThings.sharedPreferences.getBoolean("demoOn", false));
        batteryPluggedSwitch.setChecked(activity.setThings.sharedPreferences.getBoolean("isCharging", false));
        if (activity.setThings.sharedPreferences.getBoolean("isCharging", false)) {
            batteryPlugged = "true";
        }
        airplaneMode.setChecked(activity.setThings.sharedPreferences.getBoolean("showAirplane", false));
        if (activity.setThings.sharedPreferences.getBoolean("showAirplane", false)) {
            showAirplane = "show";
        }
        showNotifSwitch.setChecked(activity.setThings.sharedPreferences.getBoolean("showNotifs", false));
        if (activity.setThings.sharedPreferences.getBoolean("showNotifs", false)) {
            showNotifs = "true";
        }

        mobileType = (String) mobileTypeSpinner.getItemAtPosition(activity.setThings.sharedPreferences.getInt("mobileType1", 0));

        hour = activity.setThings.sharedPreferences.getInt("hour", 12);
        minute = activity.setThings.sharedPreferences.getInt("minute", 00);
        mobileLevel = activity.setThings.sharedPreferences.getInt("mobileLevel", 3);
        wifiLevel = activity.setThings.sharedPreferences.getInt("wifiLevel", 3);
        batteryLevel = activity.setThings.sharedPreferences.getInt("batteryLevel", 50);

        //button and switch listeners

        /* TODO: clean up listeners */
        activity.setThings.buttons(enableDemo, "enableDemo");
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

    private void setSpinnerAdapters(Spinner spinner, int arrayID) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity.getApplicationContext(),
                arrayID, activity.setThings.Dark ? R.layout.spinner_items_dark : R.layout.spinner_items_light); //set correct spinner text color
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setStatBarStyle(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activity.setThings.editor.putInt("statBarStyle1", position);
                activity.setThings.editor.apply();
                statBarStyle = String.valueOf(spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setShowNotifs(Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.setThings.editor.putBoolean("showNotifs", true);
                    showNotifs = "true";
                } else {
                    activity.setThings.editor.putBoolean("showNotifs", false);
                    showNotifs = "false";
                }
                activity.setThings.editor.apply();
            }
        });
    }

    private void setBatteryPlugged(Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.setThings.editor.putBoolean("isCharging", true);
                    batteryPlugged = "true";
                } else {
                    activity.setThings.editor.putBoolean("isCharging", false);
                    batteryPlugged = "false";
                }
                activity.setThings.editor.apply();
            }
        });
    }

    private void setAirplaneMode(Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.setThings.editor.putBoolean("showAirplane", true);
                    showAirplane = "show";
                } else {
                    activity.setThings.editor.putBoolean("showAirplane", false);
                    showAirplane = "hide";
                }
                activity.setThings.editor.apply();
            }
        });
    }

    private void selectMobileType(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = String.valueOf(spinner.getItemAtPosition(position));
                activity.setThings.editor.putInt("mobileType1", position);
                activity.setThings.editor.apply();
                mobileType = value;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void selectBatteryLevel(final Button button) {
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
                np.setValue(activity.setThings.sharedPreferences.getInt("batteryLevel", 50));
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
                        activity.setThings.editor.putInt("batteryLevel", np.getValue());
                        activity.setThings.editor.apply();
                        batteryLevel = np.getValue();
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
    }

    private void selectTime(final Button button) {
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
                        activity.setThings.editor.putInt("hour", hourOfDay);
                        demo.minute = minute;
                        activity.setThings.editor.putInt("minute", minute);
                        activity.setThings.editor.apply();
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle(getResources().getText(R.string.select_time_title));
                timePickerDialog.show();
            }
        });
    }

    private void selectSignal(final int signalID, final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (signalID) {
                    case R.id.wifi_strength:
                        wifiLevel = Integer.decode(String.valueOf(spinner.getItemAtPosition(position)));
                        activity.setThings.editor.putInt("wifiLevel", wifiLevel);
                        break;
                    case R.id.mobile_strength:
                        mobileLevel = Integer.decode(String.valueOf(spinner.getItemAtPosition(position)));
                        activity.setThings.editor.putInt("mobileLevel", mobileLevel);
                }
                activity.setThings.editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDemo(final Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            try {

                                //create new Intent and add relevant data to show Demo Mode with specified options
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

                                activity.setThings.editor.putBoolean("demoOn", true);
                                activity.setThings.editor.apply();
                            } catch (Exception e) {
                                Log.e("Demo", e.getMessage());
                            }
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            //turn Demo Mode off
                            Intent intent = new Intent("com.android.systemui.demo");
                            intent.putExtra("command", "exit");
                            getActivity().sendBroadcast(intent);
                            activity.setThings.editor.putBoolean("demoOn", false);
                            activity.setThings.editor.apply();
                        }
                    }).start();
                }
            }
        });
    }
}
