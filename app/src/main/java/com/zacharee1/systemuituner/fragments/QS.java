package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 4/5/2017.
 */

@SuppressWarnings("ALL")
public class QS extends Fragment {
    private MainActivity activity;
    private View view;

    private String SECURE = "secure";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_qs, container, false);

        setupQQS();
        setupSwitches();

        return view;
    }

    private void setupQQS() {
        CardView qqs = (CardView) view.findViewById(R.id.sysui_qqs_count_card);
        if (activity.setThings.SDK_INT < 24) qqs.setVisibility(View.GONE);
        else {
            final TextView count = (TextView) view.findViewById(R.id.sysui_qqs_count_text);
            SeekBar slider = (SeekBar) view.findViewById(R.id.sysui_qqs_count);
            int progress = android.provider.Settings.Secure.getInt(activity.getContentResolver(), "sysui_qqs_count", 6);
            if (progress > 10) progress = 10;

            slider.setProgress(progress);
            count.setText(String.valueOf(progress));

            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    activity.setThings.settings(SECURE, "sysui_qqs_count", progress + "");
                    count.setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    private void setupSwitches() {
        Switch fancy_anim = (Switch) view.findViewById(R.id.allow_fancy_animation);
        Switch move_whole_rows = (Switch) view.findViewById(R.id.move_full_rows);

        fancy_anim.setChecked(Settings.Secure.getInt(activity.getContentResolver(), "sysui_qs_fancy_anim", 1) == 1);
        move_whole_rows.setChecked(Settings.Secure.getInt(activity.getContentResolver(), "sysui_qs_move_whole_rows", 1) == 1);

        fancy_anim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.setThings.settings(SECURE, "sysui_qs_fancy_anim", isChecked ? "1" : "0");
            }
        });

        move_whole_rows.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.setThings.settings(SECURE, "sysui_qs_move_whole_rows", isChecked ? "1" : "0");
            }
        });
    }
}
