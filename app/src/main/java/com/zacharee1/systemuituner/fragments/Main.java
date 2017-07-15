package com.zacharee1.systemuituner.fragments;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

/**
 * Created by Zacha on 4/2/2017.
 */

public class Main extends Fragment {
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) activity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView title = view.findViewById(R.id.title_main);
        title.setTextColor(activity.setThings.titleText); //set title text to the correct color for dark/light mode

        Button donate = view.findViewById(R.id.donate_paypal);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setThings.donate();
            }
        });

        return view;
    }
}
