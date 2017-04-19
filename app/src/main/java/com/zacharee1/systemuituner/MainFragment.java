package com.zacharee1.systemuituner;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Zacha on 4/2/2017.
 */

public class MainFragment extends Fragment {
    public View view;
    public MainActivity activity;

    Button setup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) activity = (MainActivity) getActivity();

        activity.setTitle("SystemUI Tuner");

        view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView title = (TextView) view.findViewById(R.id.title_main);
        title.setTextColor(activity.setThings.titleText);

        setup = (Button) view.findViewById(R.id.setup);
        activity.setThings.buttons(setup, "setup");

        return view;
    }
}
