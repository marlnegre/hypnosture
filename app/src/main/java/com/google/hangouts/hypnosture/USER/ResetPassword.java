package com.google.hangouts.hypnosture.USER;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.hangouts.hypnosture.R;

public class ResetPassword extends AppCompatActivity {

    EditText passwordEmail;
    Button resetPassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        passwordEmail = findViewById(R.id.resetPasswordEmail);
        resetPassword = findViewById(R.id.resetPasswordBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = passwordEmail.getText().toString().trim();

                if(userEmail.isEmpty()){
                    Toast.makeText(ResetPassword.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassword.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ResetPassword.this, MainActivity.class));
                            }else{
                                Toast.makeText(ResetPassword.this, "Error in sending password reset", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });
    }
}
