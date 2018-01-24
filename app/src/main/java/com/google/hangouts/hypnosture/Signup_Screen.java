package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Signup_Screen extends AppCompatActivity implements View.OnClickListener{

    ProgressBar progress;
    EditText username, email, password, confirm;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__screen);


        findViewById(R.id.signupbutton).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        refs();
    }

    private void registerUser(){
        String User = username.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Confirm= confirm.getText().toString().trim();

        if(User.isEmpty()){
            username.setError("Username must not be empty.");
            username.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            email.setError("Email must not be empty.");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            password.setError("Password is required.");
            password.requestFocus();
            return;
        }

        if(Password.length() < 6){
            password.setError("Minimum length of the password must be 6");
            password.requestFocus();
            return;
        }

        if(Confirm.isEmpty()){
            confirm.setError("Confirm Password is required.");
            confirm.requestFocus();
            return;
        }
        if(Confirm.length() < 6){
            confirm.setError("Minimum length must be 6");
            confirm.requestFocus();
            return;
        }
        if(!Confirm.equals(Password)){
            confirm.setError("Must match with the inputted previous password");
            confirm.requestFocus();
            return;
        }

        progress.setVisibility(View.VISIBLE);

           mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            finish();
                            Intent intent = new Intent(Signup_Screen.this, EditProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "User Already Registered!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Some error occurred.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void refs(){
        username =  findViewById(R.id.editText4);
        email = findViewById(R.id.editText5);
        password =  findViewById(R.id.editText6);
        confirm = findViewById(R.id.editText3);
        progress = findViewById(R.id.progressbar);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signupbutton:
                username.requestFocus();
                registerUser();
                break;
        }
    }


}
