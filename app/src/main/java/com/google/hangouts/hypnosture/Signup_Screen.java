package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Signup_Screen extends AppCompatActivity {

    Button signup;
    EditText username, email, password, confirm;
    private FirebaseAuth mAuth;

    public static final String USER_ID = "artistid";
    public static final String USER_NAME = "artistname";

    DatabaseReference databaseUsers;
    FirebaseDatabase usersDatabase;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__screen);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        usersDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.editText4);
        email = findViewById(R.id.editText5);
        password = findViewById(R.id.editText6);
        confirm = findViewById(R.id.editText3);
        signup = findViewById(R.id.submitSignup);

      signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String User = username.getText().toString().trim();
                final String Email = email.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                final String Confirm = confirm.getText().toString().trim();

                if (User.isEmpty() || Email.isEmpty() || Password.isEmpty() || Confirm.isEmpty()){
                    Toast.makeText(Signup_Screen.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
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

                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(User);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(Signup_Screen.this, "Choose a different username.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            registerUser();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
            }
        });
    }


    public void registerUser() {

        final String User = username.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {


                            String id = databaseUsers.push().getKey();

                            User newUser = new User(id, User, Email, Password);

                            databaseUsers.child(id).setValue(newUser);

//                           // Intent intent = new Intent(Signup_Screen.this, EditProfileActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.putExtra(USER_ID, newUser.getUserId());
//                            intent.putExtra(USER_NAME, newUser.getUsername());
//                            startActivity(intent);

                            finish();

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User Already Registered!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Some error occurred.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}