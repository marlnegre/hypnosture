package com.google.hangouts.hypnosture.USER;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.hangouts.hypnosture.Activity_Homescreen;
import com.google.hangouts.hypnosture.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class Signup_Screen extends AppCompatActivity {

    private static final String TAG = "Signup_Screen";

    Button signup;
    EditText fname, lname, email, password, confirm;
    ProgressDialog mProgress;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String userID;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__screen);

        mListView = findViewById(R.id.listView);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Users");

        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText5);
        password = findViewById(R.id.editText6);
        confirm = findViewById(R.id.editText3);
        signup = findViewById(R.id.submitSignup);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    finish();
                    startActivity(new Intent(Signup_Screen.this, Activity_Homescreen.class));
                }
            }
        };



      signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                final String Confirm = confirm.getText().toString().trim();
                final String Fname = fname.getText().toString();
                final String Lname = lname.getText().toString();

                Pattern regexx = Pattern.compile("^[a-zA-Z]+$");


                if (Email.isEmpty()){
                    email.setError("Email is empty");
                    email.requestFocus();
                    return;
                }
                if (Password.isEmpty()){
                    password.setError("Password is empty");
                    password.requestFocus();
                    return;
                }
                if (Confirm.isEmpty()){
                    confirm.setError("Confirm Password is empty");
                    confirm.requestFocus();
                    return;
                }
                if (Fname.isEmpty()){
                    fname.setError("First Name is empty");
                    fname.requestFocus();
                    return;
                }
                if (Lname.isEmpty()){
                    lname.setError("Last Name is empty");
                    lname.requestFocus();
                    return;
                }

                if (!(regexx.matcher(Fname).find())) {
                    fname.setError("Must contain alphabets only");
                    fname.requestFocus();
                    return;
                }
                if (!(regexx.matcher(Lname).find())) {
                    lname.setError("Must contain alphabets only");
                    lname.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }
                if (Password.length() < 6) {
                    password.setError("Minimum length must be 6");
                    password.requestFocus();
                    return;
                }
                if (!Confirm.equals(Password)) {
                    confirm.setError("Must match with the inputted previous password");
                    confirm.requestFocus();
                    return;
                }
                    registerUser();

            }
        });
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Signup_Screen.this, "Successfully Registered, Email verification sent", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    finish();
                    startActivity(new Intent(Signup_Screen.this, MainActivity.class));
                }
                else{
                    Toast.makeText(Signup_Screen.this, "Email verification hasn't been sent!", Toast.LENGTH_SHORT).show();
                }
                }
            });
        }
    }


    public void registerUser() {

        final String Email = email.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Fname = fname.getText().toString();
        final String Lname = lname.getText().toString();

        mAuth.createUserWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    mProgress.setTitle("Registering User");
                    mProgress.setMessage("Please wait...");
                    mProgress.show();

                    FirebaseUser user = mAuth.getCurrentUser();
                    userID = user.getUid();

                    User newUser = new User(userID, Email, Password, Fname, Lname);
                    myRef.child(userID).setValue(newUser);
                    sendEmailVerification();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "User Already Registered!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Failed! Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
                }
            });
    }
}
