package com.google.hangouts.hypnosture.ADMIN;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.R;

public class VerifiedAdmin extends AppCompatActivity {

    EditText verify;
    Button verifyBtn;
    DatabaseReference databaseVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_admin);

        verify = findViewById(R.id.adminVerificationEditText);
        verifyBtn = findViewById(R.id.submitBtn);
        databaseVerification = FirebaseDatabase.getInstance().getReference("Verification");
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddVerification();
            }
        });
    }

    private void AddVerification() {
        String verification = verify.getText().toString();

        if(verification.isEmpty()){
            verify.setError("Field is empty!");
            verify.requestFocus();
            return;
        }

       Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Verification").orderByChild("verificationPIN").equalTo(verification);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                Toast.makeText(VerifiedAdmin.this, "Admin Verified", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(VerifiedAdmin.this, AdminLogin.class));
                                finish();
                            }
                            else {
                                Toast.makeText(VerifiedAdmin.this, "Admin not verified.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


    }
}
