package com.zacharee1.systemuituner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public SetThings setThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = new Fragment();
        final Fragment finalFragment;
        final FragmentManager fragmentManager = getFragmentManager();
        final Handler handler = new Handler();

        if (id != R.id.nav_exit) {
            setThings.editor.putInt("navpage", id);
            setThings.editor.apply();
        }

        if (id == R.id.nav_home) fragment = new MainFragment();
        else if (id == R.id.nav_quick_settings) fragment = new QSFragment();
        else if (id == R.id.nav_statusbar) fragment = new StatBarFragment();
        else if (id == R.id.nav_demo_mode) fragment = new DemoFragment();
        else if (id == R.id.nav_about) fragment = new AboutFragment();
        else if (id == R.id.nav_settings) fragment = new SettingsFragment();
        else if (id == R.id.nav_misc) fragment = new MiscFragment();
        else if (id == R.id.nav_exit) super.onBackPressed();

        finalFragment = fragment;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager.beginTransaction().replace(R.id.content_main, finalFragment).commit();
            }
        }, 350);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setup() {
        Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
        if (!setThings.setup) startActivity(intent);

        int id = setThings.sharedPreferences.getInt("navpage", R.id.nav_home);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu navMenu = navigationView.getMenu();

        if (setThings.pages.contains(id)) navMenu.findItem(id).setChecked(true);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment;

        if (id == R.id.nav_home) fragment = new MainFragment();
        else if (id == R.id.nav_quick_settings) fragment = new QSFragment();
        else if (id == R.id.nav_statusbar) fragment = new StatBarFragment();
        else if (id == R.id.nav_demo_mode) fragment = new DemoFragment();
        else if (id == R.id.nav_about) fragment = new AboutFragment();
        else if (id == R.id.nav_settings) fragment = new SettingsFragment();
        else if (id == R.id.nav_misc) fragment = new MiscFragment();
        else fragment = new MainFragment();

        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

        navigationView.setItemIconTintList(setThings.drawerItem);
        navigationView.setItemTextColor(setThings.drawerItem);
    }
}
