package com.google.hangouts.hypnosture;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import  android.view.View;
import  android.widget.Button;
import  android.content.Intent;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static int SPLASH_TIME_OUT = 4000;

    Button login_Button, signup_button;
    EditText username, password;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

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



        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.signupbtn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        refs();


    }
    public void loginUser(){
        String userLogin = username.getText().toString().trim();
        String passLogin = password.getText().toString().trim();

        if(userLogin.isEmpty()){
            username.setError("Username or Email is required");
            username.requestFocus();
            return;
        }
        if(passLogin.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(passLogin.length() < 6){
            password.setError("Minimum length of password must be 6");
            password.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(userLogin, passLogin)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, Calibrate.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void refs(){
        username =  findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        login_Button = findViewById(R.id.login);
        signup_button = findViewById(R.id.signupbtn);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login:
                loginUser();
                break;
            case R.id.signupbtn:
                startActivity(new Intent(this, Signup_Screen.class));
                break;
        }
    }


}
