package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by christian Kent Igot on 16/02/2018.
 */


public class Activity_Homescreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        //firebase
        mAuth = FirebaseAuth.getInstance();

        //firebase
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        setTitle("Home");
        transaction.replace(R.id.frame, new Fragment_Home()).commit();


    }

    //Bottom nav
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Home");
                    transaction.replace(R.id.frame, new Fragment_Home()).commit();
                    return true;

                case R.id.navigation_dashboard:
                    setTitle("Gallery");
                    transaction.replace(R.id.frame, new Fragment_Gallery()).commit();
                    return true;

                case R.id.navigation_exercise:
                    setTitle("Exercises");

                    transaction.replace(R.id.frame, new Fragment_Exercise()).commit();
                    return true;

            }
            return false;
        }
    };

    //Bottom nav

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        int id = item.getItemId();

        switch(id){

            case R.id.drawer_profile:
                Intent s0 = new Intent(Activity_Homescreen.this, Activity_Profile.class);
                startActivity(s0);
                break;
            case R.id.drawer_stat:
                Intent s1 = new Intent(Activity_Homescreen.this, Activity_PerformanceStat.class);
                startActivity(s1);
                break;
            case R.id.drawer_recommendation:
                Intent s2 = new Intent(Activity_Homescreen.this, Activity_Recommendations.class);
                startActivity(s2);
                break;
            case R.id.drawer_snore:
                Intent s3 = new Intent(Activity_Homescreen.this, SnoreTrackActivity.class);
                startActivity(s3);
                break;
            case R.id.navigation_settings:
                Intent s4 = new Intent(Activity_Homescreen.this, SettingsActivity.class);
                startActivity(s4);
                break;
            case R.id.drawer_exercise:
                Intent s5 = new Intent(Activity_Homescreen.this, ExercisesActivity.class);
                startActivity(s5);
                break;
            case R.id.drawer_bluetooth:
                Intent s6 = new Intent(Activity_Homescreen.this, Activity_Bluetooth.class);
                startActivity(s6);
                break;
            case R.id.drawer_logout:
                mAuth.signOut();
                finish();
                break;


        }
        return true;
    }

}
