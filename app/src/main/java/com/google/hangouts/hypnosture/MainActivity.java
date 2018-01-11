package com.google.hangouts.hypnosture;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import  android.view.View;
import  android.widget.Button;
import  android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;

    Button login_Button, signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //**   new Handler().postDelayed(new Runnable() {
     //       @Override
      //      public void run() {
      ///          Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
      //          startActivity(homeIntent);
      //      }
     //   },SPLASH_TIME_OUT);



        refs();

        login_Button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View V) {
                        Intent myIntent = new Intent(V.getContext(), Homescreen.class);
                        startActivity(myIntent);
                    }
                }

        );
        signup_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View V) {
                        Intent signupIntent = new Intent(V.getContext(), Signup_Screen.class);
                        startActivity(signupIntent);
                    }
                }

        );
    }

    public void refs() {
        login_Button = (Button) findViewById(R.id.login);
        signup_button = (Button) findViewById(R.id.Signup);

    }
}
