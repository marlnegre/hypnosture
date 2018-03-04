package com.google.hangouts.hypnosture.USER;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity{

    ImageButton updateBtn, changepassBtn;
    private CircleImageView circleImageView;
    DatabaseReference mUsersDatabase;
    private TextView fullname, birthday, sex, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setBackBtn();

        updateBtn = findViewById(R.id.imageviewUpdateProfile);
        changepassBtn = findViewById(R.id.imageviewChangePassword);

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        circleImageView = findViewById(R.id.circleImageView);
        email = findViewById(R.id.textViewEmail);
        fullname = findViewById(R.id.editTextFullName);
        birthday = findViewById(R.id.editTextBirthday);
        sex = findViewById(R.id.editTextSex);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, UpdateProfile.class));
                Toast.makeText(UserProfile.this, "Double-click birthday", Toast.LENGTH_SHORT).show();
            }
        });

        changepassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, ChangingPassword.class));
            }
        });

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    String PhotoUrl = dataSnapshot.child("profilePicURL").getValue().toString();
                    String Email = dataSnapshot.child("email").getValue().toString();
                    String Fname = dataSnapshot.child("fname").getValue().toString();
                    String Lname = dataSnapshot.child("lname").getValue().toString();
                    String Sex = dataSnapshot.child("sex").getValue().toString();
                    String Birthday = dataSnapshot.child("birthday").getValue().toString();

                    Glide.with(UserProfile.this).load(PhotoUrl).into(circleImageView);
                    email.setText(Email);
                    fullname.setText(Fname + " " + Lname);
                    sex.setText(Sex);
                    birthday.setText(Birthday);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setBackBtn() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
}
