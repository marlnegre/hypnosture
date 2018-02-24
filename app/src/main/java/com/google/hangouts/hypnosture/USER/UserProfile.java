package com.google.hangouts.hypnosture.USER;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private CircleImageView circleImageView;
    private DatabaseReference mUsersDatabase;
    private TextView fullname, birthday, sex, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setBackBtn();

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        circleImageView = findViewById(R.id.circleImageView);
        email = findViewById(R.id.textViewEmail);
        fullname = findViewById(R.id.editTextFullName);
        birthday = findViewById(R.id.editTextBirthday);
        sex = findViewById(R.id.editTextSex);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    String PhotoUrl = dataSnapshot.child("profilePicURL").getValue().toString();
                    String Email = dataSnapshot.child("email").getValue().toString();
                    String Fname = dataSnapshot.child("fname").getValue().toString();
                    String Sex = dataSnapshot.child("sex").getValue().toString();
                    String Birthday = dataSnapshot.child("birthday").getValue().toString();

                    Glide.with(UserProfile.this).load(PhotoUrl).into(circleImageView);
                    email.setText(Email);
                    fullname.setText(Fname);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options3, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changepass:
                startActivity(new Intent(this, ChangingPassword.class));
                return true;
            case R.id.editprofile:
                startActivity(new Intent(this, UpdateProfile.class));
                return true;
            case R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
