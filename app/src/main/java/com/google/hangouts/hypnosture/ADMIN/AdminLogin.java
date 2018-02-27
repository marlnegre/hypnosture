package com.google.hangouts.hypnosture.ADMIN;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.Activity_Homescreen;
import com.google.hangouts.hypnosture.USER.ChangingPassword;
import com.google.hangouts.hypnosture.R;
import com.google.hangouts.hypnosture.USER.MainActivity;
import com.google.hangouts.hypnosture.USER.Signup_Screen;
import com.google.hangouts.hypnosture.USER.UpdateProfile;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener{


    Button login_Button, signup_button;
    EditText userEmail, password;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference users;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        findViewById(R.id.signupbtn).setOnClickListener(this);
        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        userEmail = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        login_Button = findViewById(R.id.login);
        signup_button = findViewById(R.id.signupbtn);


        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userLogin = userEmail.getText().toString().trim();
                final String passLogin = password.getText().toString().trim();

                if(userLogin.isEmpty() || passLogin.isEmpty()){
                    Toast.makeText(AdminLogin.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passLogin.length() < 6){
                    password.setError("Minimum length of password must be 6");
                    password.requestFocus();
                    return;
                }
                final Query passwordQuery = FirebaseDatabase.getInstance().getReference().child("Admins").orderByChild("password").equalTo(passLogin);
                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Admins").orderByChild("email").equalTo(userLogin);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mProgress.setTitle("Logging in User");
                        mProgress.setMessage("Please wait...");
                        mProgress.show();
                        if (dataSnapshot.getChildrenCount() > 0) {
                            passwordQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        loginUser();
                                        Toast.makeText(AdminLogin.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AdminLogin.this, UsersActivity.class));
                                        mProgress.dismiss();
                                        userEmail.setText("");
                                        password.setText("");
                                    } else {
                                        Toast.makeText(AdminLogin.this, "Password is wrong!", Toast.LENGTH_SHORT).show();
                                        mProgress.dismiss();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }else {
                            Toast.makeText(AdminLogin.this, "Admin account may not be registered!", Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    public void loginUser(){
        String userLogin = userEmail.getText().toString().trim();
        String passLogin = password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(userLogin, passLogin)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            mProgress.setTitle("Logging in admin");
                            mProgress.setMessage("Please wait...");
                            mProgress.show();

                            Intent intent = new Intent(AdminLogin.this, UsersActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, UsersActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signupbtn:
                startActivity(new Intent(this, AdminSignup_Screen.class));
                break;
        }
    }
    }

