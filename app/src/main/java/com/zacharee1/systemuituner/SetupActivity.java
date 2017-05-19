package com.zacharee1.systemuituner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroBase;
import com.github.paolorotolo.appintro.AppIntroBaseFragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.Toast;

public class SetupActivity extends AppIntro2 {

    private BroadcastReceiver finish_activity;
    private SetThings setThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);
        //activity = this;

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);

        int backgroundColor = typedValue.data;

        AppIntroFragment introFrag = AppIntroFragment.newInstance(getResources().getText(R.string.app_name).toString(), getResources().getText(R.string.welcome).toString(), R.drawable.ic_launcher_large, backgroundColor);
        AppIntroBaseFragment askRootFrag = AskRoot.newInstance(getResources().getText(R.string.setup).toString(), getResources().getText(R.string.setup_ask).toString(), 0, backgroundColor);
        AppIntroBaseFragment noRootFrag = NoRoot.newInstance(getResources().getText(R.string.no_root_setup).toString(), getResources().getText(R.string.adb_instructions).toString(), 0, backgroundColor);
        AppIntroFragment doneFrag = AppIntroFragment.newInstance(getResources().getText(R.string.done_button).toString(), "", R.drawable.ic_check_accent, backgroundColor);

        addSlide(introFrag);
        addSlide(askRootFrag);
        addSlide(noRootFrag);
        addSlide(doneFrag);

        showSkipButton(false);
        setSwipeLock(true);

        finish_activity = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_systemuituner_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(finish_activity, new IntentFilter("finish_systemuituner_activity"));
    }

    @Override
    public void setProgressButtonEnabled(boolean bool) {
        super.setProgressButtonEnabled(bool);

        setSwipeLock(true);

        if (pager.getCurrentItem() == 0 || pager.getCurrentItem() == 2) {
            setButtonState(backButton, false);
        } else {
            setButtonState(backButton, true);
        }

        if (pager.getCurrentItem() == 1) {
            setButtonState(backButton, false);
            setButtonState(nextButton, false);
        }
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(finish_activity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    public void onDonePressed(Fragment current) {
        boolean canFinish;
        try {
            Settings.Secure.putInt(getContentResolver(), "systemui_tuner_test", 1);
            canFinish = true;
            setThings.editor.putBoolean("isSetup", true);
            setThings.editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getResources().getText(R.string.permissions_failed), Toast.LENGTH_LONG).show();
            canFinish = false;
        }
        if (canFinish) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static class AskRoot extends AppIntroBaseFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);

            final SetupActivity activity = (SetupActivity) getActivity();
            final Fragment fragment = this;

            Button rooted = (Button) (view != null ? view.findViewById(R.id.rooted) : null);

            if (rooted != null) rooted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (testSudo()) {
                        sudo("pm grant com.zacharee1.systemuituner android.permission.WRITE_SECURE_SETTINGS ; " +
                                "pm grant com.zacharee1.systemuituner android.permission.DUMP");
                        activity.setThings.editor.putBoolean("isRooted", true);
                        activity.setThings.editor.apply();
                        for (int i = activity.fragments.indexOf(fragment); i < activity.fragments.size(); i++) {
                            activity.nextButton.performClick();
                        }

                    } else {
                        Toast.makeText(activity, getResources().getText(R.string.root_test_failed), Toast.LENGTH_SHORT).show();
                        activity.setThings.editor.putBoolean("isRooted", false);
                        activity.nextButton.performClick();
                    }
                }
            });

            return view;
        }

        public static AskRoot newInstance(String title, String description, @SuppressWarnings("SameParameterValue") @DrawableRes int drawable, @ColorInt int color) {
            AskRoot fragment = new AskRoot();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            args.putString(ARG_DESC, description);
            args.putInt(ARG_DRAWABLE, drawable);
            args.putInt(ARG_BG_COLOR, color);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        protected int getLayoutId() {
            return R.layout.activity_setup;
        }

        private boolean testSudo() {
            StackTraceElement st = null;

            try{
                final Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                outputStream.writeBytes("exit\n");
                outputStream.flush();

                DataInputStream inputStream = new DataInputStream(su.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                while (bufferedReader.readLine() != null) {
                    bufferedReader.readLine();
                }

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        su.destroy();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getResources().getText(R.string.root_test_failed), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };

                Timer timer = new Timer();
                timer.schedule(task, 10000);

                su.waitFor();
                timer.cancel();
                timer.purge();
            } catch (Exception e) {
                e.printStackTrace();
                for (StackTraceElement s : e.getStackTrace()) {
                    st = s;
                    if (st != null) break;
                }
            }

            return st == null;
        }

        private void sudo(String... strings) {
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
                    Log.e("No Root?", e.getMessage());
                }
                outputStream.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public static class NoRoot extends AppIntroBaseFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            TextView adb_instructions = (TextView) (view != null ? view.findViewById(R.id.adb_instructions) : null);
            adb_instructions.setMovementMethod(LinkMovementMethod.getInstance());
            return view;
        }

        public static NoRoot newInstance(String title, String description, @SuppressWarnings("SameParameterValue") @DrawableRes int drawable, @ColorInt int color) {
            NoRoot fragment = new NoRoot();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            args.putString(ARG_DESC, description);
            args.putInt(ARG_DRAWABLE, drawable);
            args.putInt(ARG_BG_COLOR, color);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getLayoutId() {
            return R.layout.content_no_root;
        }
    }
}
