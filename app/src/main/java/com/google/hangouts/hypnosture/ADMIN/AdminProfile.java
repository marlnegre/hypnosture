package com.google.hangouts.hypnosture.ADMIN;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.google.hangouts.hypnosture.R;
import com.google.hangouts.hypnosture.USER.ChangingPassword;
import com.google.hangouts.hypnosture.USER.MainActivity;
import com.google.hangouts.hypnosture.USER.Signup_Screen;
import com.google.hangouts.hypnosture.USER.UpdateProfile;
import com.google.hangouts.hypnosture.USER.UserProfile;
import com.google.hangouts.hypnosture.UserAdmin;

import java.io.Console;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfile extends AppCompatActivity {

    ImageButton updateBtn, changepassBtn, deleteBtn;
    private CircleImageView circleImageView;
    DatabaseReference mUsersDatabase;
    private TextView fullname, birthday, sex, email;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        updateBtn = findViewById(R.id.imageviewUpdateProfile);
        changepassBtn = findViewById(R.id.imageviewChangePassword);
        deleteBtn = findViewById(R.id.imageviewDeleteAccount);
        mAuth = FirebaseAuth.getInstance();

        circleImageView = findViewById(R.id.circleImageView);
        email = findViewById(R.id.textViewEmail);
        fullname = findViewById(R.id.editTextFullName);
        birthday = findViewById(R.id.editTextBirthday);
        sex = findViewById(R.id.editTextSex);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProfile.this, AdminUpdateProfile.class));
            }
        });

        changepassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProfile.this, AdminChangePassword.class));
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference admins = FirebaseDatabase.getInstance().getReference("Admins").child(user_id);
                admins.removeValue();

                if (user != null) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mAuth.signOut();
                                Toast.makeText(AdminProfile.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminProfile.this, UserAdmin.class));
                                finish();
                            } else {
                                Toast.makeText(AdminProfile.this, "Account cannot be deleted!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    startActivity(new Intent(AdminProfile.this, UsersActivity.class));
                    finish();
                }
            }
        });

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Admins").child(user_id);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String PhotoUrl = dataSnapshot.child("profilePicURL").getValue().toString();
                String Email = dataSnapshot.child("email").getValue().toString();
                String Fname = dataSnapshot.child("fname").getValue().toString();
                String Sex = dataSnapshot.child("sex").getValue().toString();
                String Birthday = dataSnapshot.child("birthday").getValue().toString();

                Glide.with(AdminProfile.this).load(PhotoUrl).into(circleImageView);
                email.setText(Email);
                fullname.setText(Fname);
                sex.setText(Sex);
                birthday.setText(Birthday);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminProfile.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void deleteAccount() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference admins = FirebaseDatabase.getInstance().getReference("Admins").child(user_id);
        admins.removeValue();

        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mAuth.signOut();
                        Toast.makeText(AdminProfile.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminProfile.this, UserAdmin.class));
                        finish();
                    } else {
                        Toast.makeText(AdminProfile.this, "Account cannot be deleted!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }*/

}
