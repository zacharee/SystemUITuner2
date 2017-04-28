package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 4/15/2017.
 */

public class Settings extends Fragment {
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch darkMode = (Switch) view.findViewById(R.id.dark_mode);
        activity.setThings.switches(darkMode, null, "dark_mode", view);

        return view;
    }
}