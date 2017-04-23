package com.zacharee1.systemuituner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zacharee1.systemuituner.fragments.About;
import com.zacharee1.systemuituner.fragments.Demo;
import com.zacharee1.systemuituner.fragments.Main;
import com.zacharee1.systemuituner.fragments.Misc;
import com.zacharee1.systemuituner.fragments.QS;
import com.zacharee1.systemuituner.fragments.Settings;
import com.zacharee1.systemuituner.fragments.StatBar;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public SetThings setThings;

    private Fragment fragment;
    private FragmentManager fragmentManager;

    private NavigationView navigationView;
    private Menu navMenu;

    private Handler handler;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);
        fragment = new Fragment();
        fragmentManager = getFragmentManager();
        handler = new Handler();
        context = this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setPopupTheme(setThings.style);

        setTitle("SystemUI Tuner");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setup();
    }

    @Override
    public void onBackPressed() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Uri uri;
        Intent intent = new Intent(Intent.ACTION_VIEW, null);
        boolean tralse = false;
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
                boolean labsInstalled = setThings.isPackageInstalled("com.xda.labs", context.getPackageManager());
                uri = Uri.parse(labsInstalled ? "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=andywander@yahoo.com" : "https://forum.xda-developers.com/donatetome.php?u=7055541");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                tralse = true;
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
        }
        startActivity(intent);

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
                }
            }, 350);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        if (id == R.id.nav_exit) super.onBackPressed();
        return true;
    }

    private void setup() {
        Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
        if (!setThings.setup) startActivity(intent);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navMenu = navigationView.getMenu();

        int id = setThings.sharedPreferences.getInt("navpage", R.id.nav_home);

        if (setThings.pages.contains(id)) navMenu.findItem(id).setChecked(true);

        fragment = chooseFrag(id);
        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

        navigationView.setItemIconTintList(setThings.drawerItem);
        navigationView.setItemTextColor(setThings.drawerItem);
    }

    private Fragment chooseFrag(int id) {
        Fragment fragment = new Fragment();
        switch (id) {
            case R.id.nav_home:
                fragment = new Main();
                break;
            case R.id.nav_quick_settings:
                fragment = new QS();
                break;
            case R.id.nav_statusbar:
                fragment = new StatBar();
                break;
            case R.id.nav_demo_mode:
                fragment = new Demo();
                break;
            case R.id.nav_about:
                fragment = new About();
                break;
            case R.id.nav_settings:
                fragment = new Settings();
                break;
            case R.id.nav_misc:
                fragment = new Misc();
                break;
            default:
                fragment = new Main();
                break;
        }

        return fragment;
    }
}
