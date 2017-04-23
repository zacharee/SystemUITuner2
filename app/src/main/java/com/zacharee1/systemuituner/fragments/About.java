package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zacharee1.systemuituner.BuildConfig;
import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

import java.util.Date;

/**
 * Created by Zacha on 4/13/2017.
 */

public class About extends Fragment {
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        if ( getActivity() instanceof MainActivity){
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("About");

        TextView versionName = (TextView) view.findViewById(R.id.vername);
        TextView versionNum = (TextView) view.findViewById(R.id.vernum);
        TextView buildDate = (TextView) view.findViewById(R.id.build_date);

        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            versionName.append(version);
            versionNum.append(String.valueOf(verCode));

            Date buildD = new Date(BuildConfig.TIMESTAMP);
            buildDate.append(String.valueOf(buildD));
        } catch (Exception e) {
            Log.e("SysUITuner/E", e.getMessage());
        }

        return view;
    }
}
