package com.zacharee1.systemuituner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.*;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.R;

import java.io.BufferedReader;

/**
 * Created by Zacha on 4/18/2017.
 */

@SuppressWarnings("ALL")
public class Misc extends Fragment {
    private View view;
    private MainActivity activity;

    private Switch show_full_zen;
    private Switch hu_notif;
    private Switch vol_warn;

    private Button animApply;
    private Button transApply;
    private Button winApply;

    private TextInputEditText anim;
    private TextInputEditText trans;
    private TextInputEditText win;

    private float animScale;
    private float transScale;
    private float winScale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        activity.setTitle("Miscellaneous"); //set proper fragment title

        view = inflater.inflate(R.layout.fragment_misc, container, false);

        show_full_zen = (Switch) view.findViewById(R.id.show_full_zen);
        hu_notif = (Switch) view.findViewById(R.id.hu_notif);
        vol_warn = (Switch) view.findViewById(R.id.vol_warn);

        animApply = (Button) view.findViewById(R.id.apply_anim);
        transApply = (Button) view.findViewById(R.id.apply_trans);
        winApply = (Button) view.findViewById(R.id.apply_win);

        anim = (TextInputEditText) view.findViewById(R.id.anim_text);
        trans = (TextInputEditText) view.findViewById(R.id.trans_text);
        win = (TextInputEditText) view.findViewById(R.id.win_text);

        animScale = Settings.Global.getFloat(activity.getContentResolver(), "animator_duration_scale", (float)1.0);
        transScale = Settings.Global.getFloat(activity.getContentResolver(), "transition_animation_scale", (float)1.0);
        winScale = Settings.Global.getFloat(activity.getContentResolver(), "window_animation_scale", (float)1.0);

        anim.setHint(getResources().getText(R.string.animator_duration_scale) + " (" + String.valueOf(animScale) + ")");
        trans.setHint(getResources().getText(R.string.transition_animation_scale) + " (" + String.valueOf(transScale) + ")");
        win.setHint(getResources().getText(R.string.window_animation_scale) + " (" + String.valueOf(winScale) + ")");

        activity.setThings.switches(show_full_zen, "sysui_show_full_zen", "secure", view); //switch listener
        activity.setThings.switches(hu_notif, "heads_up_notifications_enabled", "global", view);
        activity.setThings.switches(vol_warn, "audio_safe_volume_state", "global", view);

        buttons(animApply);
        buttons(transApply);
        buttons(winApply);

        textFields(anim);
        textFields(trans);
        textFields(win);

        return view;
    }

    private void textFields(final TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textInputEditText.getText().length() > 0) {
                    float val = Float.valueOf(textInputEditText.getText().toString());

                    if (textInputEditText == anim) animScale = val;
                    else if (textInputEditText == trans) transScale = val;
                    else if (textInputEditText == win) winScale = val;
                }
            }
        });
    }

    private void buttons(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pref = new String();
                float val = (float)1.0;

                if (button == animApply) {
                    pref = "animator_duration_scale";
                    val = animScale;
                } else if (button == transApply) {
                    pref = "transition_animation_scale";
                    val = transScale;
                } else if (button == winApply) {
                    pref = "window_animation_scale";
                    val = winScale;
                }

                Settings.Global.putFloat(activity.getContentResolver(), pref, val);
            }
        });
    }
}
