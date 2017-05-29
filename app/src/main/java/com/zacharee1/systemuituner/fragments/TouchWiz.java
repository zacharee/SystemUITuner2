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
public class TouchWiz extends Fragment {
    private MainActivity activity;
    private View view;

    private String SECURE = "secure";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        view = inflater.inflate(R.layout.fragment_tw, container, false);

        return view;
    }
}
