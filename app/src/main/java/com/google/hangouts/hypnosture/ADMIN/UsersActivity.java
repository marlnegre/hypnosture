package com.google.hangouts.hypnosture.ADMIN;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.hangouts.hypnosture.R;
import com.google.hangouts.hypnosture.USER.Signup_Screen;
import com.google.hangouts.hypnosture.USER.User;
import com.google.hangouts.hypnosture.USER.UserProfile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Administrator");

        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersList = findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<User, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(
                User.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUsersDatabase.orderByChild("fname")
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, User users, int position) {
                usersViewHolder.setFullname(users.getFname());
//                usersViewHolder.setPhotoUrl(users.getProfilePicURL(), getApplicationContext());
                usersViewHolder.setEmail(users.getEmail());

                final String user_id = getRef(position).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(UsersActivity.this, UsersProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setFullname(String name){
            TextView nameView = mView.findViewById(R.id.textViewName);
            nameView.setText(name);
        }
        public void setPhotoUrl(String photoUrl, Context ctx){
            CircleImageView userImageView = mView.findViewById(R.id.profileImage);
            Glide.with(ctx).load(photoUrl).into(userImageView);
        }
        public void setEmail(String email){
            TextView emailView = mView.findViewById(R.id.textViewEmail);
            emailView.setText(email);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.angle:
                startActivity(new Intent(this, AdminAngle.class));
                return true;
            case R.id.user:
                startActivity(new Intent(this, AdminCreateUser.class));
                return true;
//            case R.id.profile:
//                startActivity(new Intent(this, AdminProfile.class));
//                return true;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(this, AdminLogin.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
