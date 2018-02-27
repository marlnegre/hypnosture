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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.ADMIN.AdminProfile;
import com.google.hangouts.hypnosture.R;
import com.google.hangouts.hypnosture.USER.ChangingPassword;
import com.google.hangouts.hypnosture.USER.UpdateProfile;
import com.google.hangouts.hypnosture.UserAdmin;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersProfileActivity extends AppCompatActivity{

    ImageButton updateBtn, changepassBtn, deleteBtn;
    private CircleImageView circleImageView;
    DatabaseReference mUsersDatabase;
    private TextView fullname, birthday, sex, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);

        setBackBtn();

        updateBtn = findViewById(R.id.imageviewUpdateProfile);
        changepassBtn = findViewById(R.id.imageviewChangePassword);
        deleteBtn = findViewById(R.id.imageviewDeleteAccount);

        String user_id = getIntent().getStringExtra("user_id");

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
                startActivity(new Intent(UsersProfileActivity.this, UpdateProfile.class));
            }
        });

        changepassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersProfileActivity.this, ChangingPassword.class));
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String PhotoUrl = dataSnapshot.child("profilePicURL").getValue().toString();
                String Email = dataSnapshot.child("email").getValue().toString();
                String Fname = dataSnapshot.child("fname").getValue().toString();
                String Sex = dataSnapshot.child("sex").getValue().toString();
                String Birthday = dataSnapshot.child("birthday").getValue().toString();

                Glide.with(UsersProfileActivity.this).load(PhotoUrl).into(circleImageView);
                email.setText(Email);
                fullname.setText(Fname);
                sex.setText(Sex);
                birthday.setText(Birthday);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UsersProfileActivity.this, "USER ACCOUNT IS DELETED", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UsersProfileActivity.this, UsersActivity.class));
                finish();
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

    private void deleteAccount(){

        String user_id = getIntent().getStringExtra("user_id");

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
        users.removeValue();
        Toast.makeText(UsersProfileActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UsersProfileActivity.this, UsersActivity.class));
        finish();

        /*if(user!=null){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UsersProfileActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(UsersProfileActivity.this, "Account cannot be deleted!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }*/
    }
}
