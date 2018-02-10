package com.google.hangouts.hypnosture;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class Calibrate extends AppCompatActivity implements View.OnClickListener
{

    ImageButton logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.logoutBtn).setOnClickListener(this);

        refs();
        setBackBtn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logoutBtn:
                submitLogout();
                break;

            case R.id.okbutton:
                startActivity(new Intent(this, Homescreen.class));
                break;
        }
    }

    public void submitLogout(){
        AlertDialog.Builder submit_logout = new AlertDialog.Builder(this);
        submit_logout.setMessage("\tAre you sure to logout? " +
                "Logging out means monitoring will be cut off. " +
                "Click 'OK' if you're sure and 'CANCEL' if not.").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(Calibrate.this, MainActivity.class));
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = submit_logout.create();
        alert.setTitle("LOGOUT?");
        alert.show();
    }
    public void refs(){
        logoutBtn = findViewById(R.id.logoutBtn);
    }

    public void setBackBtn() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
