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
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.USER.MainActivity;
import com.google.hangouts.hypnosture.USER.UserProfile;

/**
 * Created by christian Kent Igot on 16/02/2018.
 */


public class Activity_Homescreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mUsersDatabase;

    TextView mName, mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        //firebase
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //
        //
        //

        //Bottom navigation
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        setTitle("Home");
        transaction.replace(R.id.frame, new Fragment_Home()).commit();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(Activity_Homescreen.this, MainActivity.class));
                }
            }
        };

        navigationView = findViewById(R.id.nav_view);

        mName = navigationView.getHeaderView(0).findViewById(R.id.navbarname);
        mEmail = navigationView.getHeaderView(0).findViewById(R.id.navbaremail);
  
       // mPic = navigationView.getHeaderView(0).findViewById(R.id.circleImageView);


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String Email = dataSnapshot.child("email").getValue().toString();
                String Fname = dataSnapshot.child("fname").getValue().toString();
                String Lname = dataSnapshot.child("lname").getValue().toString();



                //Glide.with(Activity_Homescreen.this).load(PhotoUrl).into(mPic);

                mEmail.setText(Email);
                mName.setText(Fname + " " + Lname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                    setTitle("Recommendations");
                    transaction.replace(R.id.frame, new Recommendation()).commit();
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        int id = item.getItemId();

        switch(id){

            case R.id.drawer_profile:
                Intent s0 = new Intent(Activity_Homescreen.this, UserProfile.class);
                startActivity(s0);
                break;
            case R.id.drawer_stat:
                Intent s1 = new Intent(Activity_Homescreen.this, PerformanceTrack.class);
                startActivity(s1);
                break;
//            case R.id.drawer_recommendation:
//                Intent s2 = new Intent(Activity_Homescreen.this, RecommendationActivity.class);
//                startActivity(s2);
//                break;
            case R.id.drawer_snore:
                Intent s3 = new Intent(Activity_Homescreen.this, SnoreTrackActivity.class);
                startActivity(s3);
                break;
            case R.id.navigation_settings:
                Intent s4 = new Intent(Activity_Homescreen.this, SettingsActivity.class);
                startActivity(s4);
                break;
            case R.id.drawer_bluetooth:
                Intent s6 = new Intent(Activity_Homescreen.this, Activity_Bluetooth.class);
                startActivity(s6);
                break;
            case R.id.drawer_logout:
                mAuth.signOut();
                Intent s7 = new Intent(Activity_Homescreen.this, MainActivity.class);
                startActivity(s7);
                break;


        }
        return true;
    }

}
