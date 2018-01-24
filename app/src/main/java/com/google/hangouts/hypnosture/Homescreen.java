package com.google.hangouts.hypnosture;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class Homescreen extends AppCompatActivity implements View.OnClickListener{

    ImageButton settingsBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        findViewById(R.id.settings_btn).setOnClickListener(this);
        findViewById(R.id.logoutBtn).setOnClickListener(this);

        refs();
    }

    public void refs() {
        logoutBtn = findViewById(R.id.logoutBtn);
        settingsBtn = findViewById(R.id.settings_btn);
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
                        startActivity(new Intent(Homescreen.this, MainActivity.class));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settings_btn:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.logoutBtn:
                submitLogout();
                break;
        }
    }

}
