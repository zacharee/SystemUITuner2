package com.zacharee1.systemuituner;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceFragment;
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Zacha on 4/5/2017.
 */

public class DemoFragment extends Fragment {
    public View view;
    public MainActivity activity;
    public SharedPreferences.Editor editor;
    public Switch enableDemo;
    public Switch showDemo;
    public Switch batteryPluggedSwitch;
    public Switch showNotifSwitch;
    public Button selectTime;
    public Button selectBatteryLevel;
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
    public String mobileType = "lte";
    public String batteryPlugged = "false";
    public String showNotifs = "false";
    public String statBarStyle = "opaque";

    boolean isRooted;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_demo, container, false);

        isRooted = activity.sharedPreferences.getBoolean("isRooted", false);

//        enableDemo = (Switch) view.findViewById(R.id.enable_demo);
        showDemo = (Switch) view.findViewById(R.id.show_demo);
        selectTime = (Button) view.findViewById(R.id.select_time);
        selectBatteryLevel = (Button) view.findViewById(R.id.select_battery_level_button);
        wifi = (Spinner) view.findViewById(R.id.wifi_strength);
        mobile = (Spinner) view.findViewById(R.id.mobile_strength);
        mobileTypeSpinner = (Spinner) view.findViewById(R.id.mobile_type);
        batteryPluggedSwitch = (Switch) view.findViewById(R.id.battery_plugged);
        showNotifSwitch = (Switch) view.findViewById(R.id.show_notifs);
        statStyleSpinner = (Spinner) view.findViewById(R.id.stat_bar_style);

        setSpinnerAdaptors(wifi, R.array.wifi_strength);
        setSpinnerAdaptors(mobile, R.array.mobile_strength);
        setSpinnerAdaptors(mobileTypeSpinner, R.array.mobile_type);
        setSpinnerAdaptors(statStyleSpinner, R.array.stat_bar_style);

        wifi.setSelection(activity.sharedPreferences.getInt("wifiLevel", 3));
        mobile.setSelection(activity.sharedPreferences.getInt("mobileLevel", 3));
        mobileTypeSpinner.setSelection(activity.sharedPreferences.getInt("mobileType1", 0));
        statStyleSpinner.setSelection(activity.sharedPreferences.getInt("statBarStyle1", 0));

        editor = activity.sharedPreferences.edit();

//        enableDemo(enableDemo);
        showDemo(showDemo);
        selectTime(selectTime);
        selectSignal(R.id.wifi_strength, wifi);
        selectSignal(R.id.mobile_strength, mobile);
        selectBatteryLevel(selectBatteryLevel);
        selectMobileType(mobileTypeSpinner);
        setBatteryPlugged(batteryPluggedSwitch);
        setShowNotifs(showNotifSwitch);
        setStatBarStyle(statStyleSpinner);
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

    public void setSpinnerAdaptors(Spinner spinner, int arrayID) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity.getApplicationContext(),
                arrayID, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setStatBarStyle(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("statBarStyle1", position);
                editor.apply();
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
                    editor.putBoolean("showNotifs", true);
                    showNotifs = "true";
                } else {
                    editor.putBoolean("showNotifs", false);
                    showNotifs = "false";
                }
                editor.apply();
            }
        });
    }

    public void setBatteryPlugged(Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("isCharging", true);
                    batteryPlugged = "true";
                } else {
                    editor.putBoolean("isCharging", false);
                    batteryPlugged = "false";
                }
                editor.apply();
            }
        });
    }

    public void selectMobileType(final Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = String.valueOf(spinner.getItemAtPosition(position));
                editor.putInt("mobileType1", position);
                editor.apply();
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
                        editor.putInt("batteryLevel", np.getValue());
                        editor.apply();
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
                        DemoFragment demo = new DemoFragment();
                        demo.hour = hourOfDay;
                        editor.putInt("hour", hourOfDay);
                        demo.minute = minute;
                        editor.putInt("minute", minute);
                        editor.apply();
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
                        editor.putInt("wifiLevel", wifiLevel);
                        break;
                    case R.id.mobile_strength:
                        mobileLevel = Integer.decode(String.valueOf(spinner.getItemAtPosition(position)));
                        editor.putInt("mobileLevel", mobileLevel);
                }
                editor.apply();
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
                            Intent intent = new Intent("com.android.systemui.demo");
                            intent.putExtra("command", "enter");
                            getActivity().sendBroadcast(intent);

                            intent.putExtra("command", "clock");
                            intent.putExtra("hhmm", String.valueOf(hour) + String.valueOf(minute));
                            getActivity().sendBroadcast(intent);

                            intent.putExtra("command", "network");
                            intent.putExtra("mobile", String.valueOf(showMobile));
                            intent.putExtra("fully", "true");
                            intent.putExtra("level", String.valueOf(mobileLevel));
                            intent.putExtra("datatype", String.valueOf(mobileType));
                            intent.putExtra("wifi", String.valueOf(showWiFi));
                            intent.putExtra("fully", "true");
                            intent.putExtra("level", String.valueOf(wifiLevel));
                            getActivity().sendBroadcast(intent);

                            intent.putExtra("command", "battery");
                            intent.putExtra("level", String.valueOf(batteryLevel));
                            intent.putExtra("plugged", String.valueOf(batteryPlugged));
                            getActivity().sendBroadcast(intent);

                            intent.putExtra("command", "notifications");
                            intent.putExtra("visible", String.valueOf(showNotifs));
                            intent.putExtra("command", "bars");
                            intent.putExtra("mode", statBarStyle);
                            getActivity().sendBroadcast(intent);
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sudo("am broadcast -a com.android.systemui.demo --es command exit");
                        }
                    }).start();
                }
            }
        });
    }

    public void sudo(String...strings) {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s+"\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
