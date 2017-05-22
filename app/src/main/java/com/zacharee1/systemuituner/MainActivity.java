package com.zacharee1.systemuituner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zacharee1.systemuituner.fragments.About;
import com.zacharee1.systemuituner.fragments.Demo;
import com.zacharee1.systemuituner.fragments.Main;
import com.zacharee1.systemuituner.fragments.Misc;
import com.zacharee1.systemuituner.fragments.QS;
import com.zacharee1.systemuituner.fragments.Settings;
import com.zacharee1.systemuituner.fragments.StatBar;
import com.zacharee1.systemuituner.iaps.IabBroadcastReceiver;
import com.zacharee1.systemuituner.iaps.IabHelper;
import com.zacharee1.systemuituner.iaps.IabResult;
import com.zacharee1.systemuituner.iaps.Purchase;
import com.zacharee1.systemuituner.receivers.ShutDownReceiver;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IabBroadcastReceiver.IabBroadcastListener {

    private static final int RC_REQUEST = 101;
    public SetThings setThings;

    private Fragment fragment;
    private FragmentManager fragmentManager;

    private Handler handler;

    private ActionBarDrawerToggle toggle;

    private Fragment main;
    private Fragment qs;
    private Fragment statbar;
    private Fragment demo;
    private Fragment about;
    private Fragment settings;
    private Fragment misc;

    private static final String donate_1 = "donate_1";
    private static final String donate_2 = "donate_2";
    private static final String donate_5 = "donate_5";
    private static final String donate_10 = "donate_10";

    private CharSequence title;
    private BroadcastReceiver finish_activity;
    private BroadcastReceiver shutDownReceiver;

    private IabHelper mHelper;

    private final String TAG = "SystemUITuner";

    private final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            try {
                mHelper.consumeAsync(purchase, null);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    };

    private IabBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);
        fragment = new Fragment();
        fragmentManager = getFragmentManager();
        handler = new Handler();

        main = new Main();
        qs = new QS();
        statbar = new StatBar();
        demo = new Demo();
        about = new About();
        settings = new Settings();
        misc = new Misc();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        shutDownReceiver = new ShutDownReceiver();
        registerReceiver(shutDownReceiver, filter);

        setContentView(R.layout.activity_main);

        String base64EncodedPublicKey = getResources().getText(R.string.dev_id).toString();
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);
            }
        });

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setPopupTheme(setThings.style);

        Toolbar toolbar = new Toolbar(this);*/
        title = getResources().getText(R.string.app_name);

        setTitle(title); //set default title just because

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        //noinspection deprecation
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary2)));
        toggle.syncState();

        //for "organization"
        setup();
    }

    @Override
    public void onBackPressed() { //the drawer is annoying to drag out, so let's open it on back press instead
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStop()
    {
        try {
            unregisterReceiver(finish_activity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            unregisterReceiver(shutDownReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Uri uri;
        Intent intent = null;
        boolean tralse = false;

        //Log.i("OPTION", String.valueOf(id) + " " + String.valueOf(R.id.home));

        //16908332 is the ID of the drawer toggle
        switch (id) {
            case R.id.action_github:
                uri = Uri.parse("https://github.com/zacharee/SystemUITuner2");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                tralse = true;
                break;
            case R.id.action_xda:
                uri = Uri.parse("https://forum.xda-developers.com/android/apps-games/app-systemui-tuner-t3588675");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                tralse = true;
                break;
            case R.id.action_labs:
                uri = Uri.parse("https://labs.xda-developers.com/store/app/com.zacharee1.systemuituner");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                tralse = true;
                break;
            case R.id.action_play:
                uri = Uri.parse("https://play.google.com/store/apps/details?id=com.zacharee1.systemuituner");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                tralse = true;
                break;
            case R.id.action_other_apps:
                uri = Uri.parse("https://play.google.com/store/apps/developer?id=Zachary+Wander");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                tralse = true;
                break;
            case R.id.action_donate:
                setThings.donate();
                break;
            case R.id.action_telegram:
                uri = Uri.parse("https://t.me/joinchat/AAAAAEIB6WKWL-yphJbZwg");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                tralse = true;
                break;
            case R.id.action_gplus:
                uri = Uri.parse("https://plus.google.com/communities/113741695211107417994");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                tralse = true;
                break;
            default:
                toggle.onOptionsItemSelected(item);
                break;
        }

        if (intent != null) startActivity(intent);

        return tralse || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_exit) {
            setThings.editor.putInt("navpage", id);
            setThings.editor.apply();

            fragment = chooseFrag(id);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
                    setTitle(title);
                }
            }, 350);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        if (id == R.id.nav_exit) { //if exit button pressed, quit app (needs to be at end of method to avoid exceptions)
            Intent intent = new Intent("finish_systemuituner_activity");
            sendBroadcast(intent);
            super.onBackPressed();
        }
        return true;
    }

    private void setup() {

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

        Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
        if (!setThings.setup) {
            startActivity(intent); //start setup activity if user hasn't run it (ie first launch)
            finish();
        }

        intent = new Intent(getApplicationContext(), WaitForBootActivity.class);
        if (!setThings.sharedPreferences.getBoolean("isBooted", true) && setThings.sharedPreferences.getBoolean("safeStatbar", false)) {
            startActivity(intent);
            finish();
        }

        if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            if (!setThings.sharedPreferences.getBoolean("samsungInitialized", false)) {
                setThings.editor.putBoolean("safeStatbar", true);
                setThings.editor.putBoolean("samsungInitialized", true);
                setThings.editor.apply();
            }
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu navMenu = navigationView.getMenu();

        /*if (setThings.SDK_INT < 23)*/ navMenu.findItem(R.id.nav_statusbar).setVisible(false);

        int id = setThings.sharedPreferences.getInt("navpage", R.id.nav_home); //nav item that was selected before last app close

        if (setThings.pages.contains(id))
            navMenu.findItem(id).setChecked(true); //make sure the ID actually exists in the app

        fragment = chooseFrag(id);
        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit(); //switch to that fragment

        //set proper nav drawer icon and text color
        navigationView.setItemIconTintList(setThings.drawerItem);
        navigationView.setItemTextColor(setThings.drawerItem);

        View header = navigationView.getHeaderView(0);
        View head = header.findViewById(R.id.nav_header);
        head.setBackground(getDrawable(setThings.Dark ? R.drawable.side_nav_bar_dark : R.drawable.side_nav_bar_light));
    }

    private Fragment chooseFrag(int id) { //return fragment corresponding to nav drawer ID
        switch (id) {
            case R.id.nav_quick_settings:
                title = getResources().getText(R.string.quick_settings);
                return qs;
            case R.id.nav_statusbar:
                title = getResources().getText(R.string.status_bar);
                return statbar;
            case R.id.nav_demo_mode:
                title = getResources().getText(R.string.demo_mode);
                return demo;
            case R.id.nav_about:
                title = getResources().getText(R.string.about);
                return about;
            case R.id.nav_settings:
                title = getResources().getText(R.string.settings);
                return settings;
            case R.id.nav_misc:
                title = getResources().getText(R.string.miscellaneous);
                return misc;
            default:
                title = getResources().getText(R.string.app_name);
                return main;
        }
    }

    @Override
    public void receivedBroadcast() {

    }

    private void complain(String message) {
        Log.e(TAG, "**** SystemUI Tuner Error: " + message);
        Toast.makeText(this, R.string.iap_fail, Toast.LENGTH_LONG).show();
//        alert("Error: " + message);
    }

    private void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    @SuppressWarnings("SameReturnValue")
    private boolean verifyDeveloperPayload(Purchase p) {
        @SuppressWarnings("UnusedAssignment") String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return false;
    }

    public void onDonate1Clicked(@SuppressWarnings("UnusedParameters") View arg0) {

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            if (mHelper != null) mHelper.flagEndAsync();
            assert mHelper != null;
            mHelper.launchPurchaseFlow(this, donate_1, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }

    public void onDonate2Clicked(@SuppressWarnings("UnusedParameters") View arg0) {

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            if (mHelper != null) mHelper.flagEndAsync();
            assert mHelper != null;
            mHelper.launchPurchaseFlow(this, donate_2, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }

    public void onDonate5Clicked(@SuppressWarnings("UnusedParameters") View arg0) {

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            if (mHelper != null) mHelper.flagEndAsync();
            assert mHelper != null;
            mHelper.launchPurchaseFlow(this, donate_5, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }
    public void onDonate10Clicked(@SuppressWarnings("UnusedParameters") View arg0) {

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            if (mHelper != null) mHelper.flagEndAsync();
            assert mHelper != null;
            mHelper.launchPurchaseFlow(this, donate_10, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (mBroadcastReceiver != null) {
                unregisterReceiver(mBroadcastReceiver);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            // very important:
            Log.d(TAG, "Destroying helper.");
            if (mHelper != null) {
                mHelper.disposeWhenFinished();
                mHelper = null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
