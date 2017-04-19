package com.zacharee1.systemuituner;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by Zacha on 4/13/2017.
 */

public class AboutFragment extends Fragment {
    View view;
    MainActivity activity;

    Button playStore;
    Button labs;
    Button XDA;

    TextView versionName;
    TextView versionNum;
    TextView buildDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);

        if ( getActivity() instanceof MainActivity){
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("About");

        TextView title = (TextView) view.findViewById(R.id.abt_title);

        if (activity.setThings.Dark) {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            title.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }

        playStore = (Button) view.findViewById(R.id.play_store);
        labs = (Button) view.findViewById(R.id.xda_store);
        XDA = (Button) view.findViewById(R.id.xda_thread);

        versionName = (TextView) view.findViewById(R.id.vername);
        versionNum = (TextView) view.findViewById(R.id.vernum);
        buildDate = (TextView) view.findViewById(R.id.build_date);

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

        buttons(playStore);
        buttons(labs);
        buttons(XDA);

        return view;
    }

    public void buttons(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                if (button == playStore) intent.setData(Uri.parse("market://details?id=com.zacharee1.systemuituner"));
                else if (button == labs) intent.setData(Uri.parse("https://labs.xda-developers.com/store/app/com.zacharee1.systemuituner"));
                else if (button == XDA) intent.setData(Uri.parse("https://forum.xda-developers.com/android/apps-games/app-systemui-tuner-t3588675"));

                startActivity(intent);
            }
        });
    }
}
