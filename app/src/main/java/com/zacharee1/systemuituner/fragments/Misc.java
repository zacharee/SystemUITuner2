package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zacharee1.systemuituner.Exceptions;
import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 4/18/2017.
 */

@SuppressWarnings("ALL")
public class Misc extends Fragment {
    private View view;
    private MainActivity activity;

    private Switch show_full_zen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("Miscellaneous");

        view = inflater.inflate(R.layout.fragment_misc, container, false);

        show_full_zen = (Switch) view.findViewById(R.id.show_full_zen);

        activity.setThings.switches(show_full_zen, "sysui_show_full_zen", "secure", view);

        return view;
    }

}
