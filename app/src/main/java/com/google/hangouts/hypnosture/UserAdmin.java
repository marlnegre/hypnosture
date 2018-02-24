package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.hangouts.hypnosture.ADMIN.AdminLogin;
import com.google.hangouts.hypnosture.USER.MainActivity;

public class UserAdmin extends AppCompatActivity {

    Button userlogin, adminlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        userlogin = findViewById(R.id.userlogin);
        adminlogin = findViewById(R.id.adminlogin);

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAdmin.this, MainActivity.class));
            }
        });

        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAdmin.this, AdminLogin.class));
            }
        });
    }
}
