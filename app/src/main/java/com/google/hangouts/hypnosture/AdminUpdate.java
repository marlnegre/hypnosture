package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AdminUpdate extends AppCompatActivity {

    Button deleteBtn;
    ImageView imageView;
    TextView textViewName;
    TextView textViewBirthday;
    TextView textViewSex;

    ListView listViewProfiles;

    DatabaseReference profileDatabase;

    List<UserProfile> profiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);


        textViewName = findViewById(R.id.textViewName);
        textViewBirthday = findViewById(R.id.textViewBirthday);
        textViewSex = findViewById(R.id.textViewSex);
        listViewProfiles = findViewById(R.id.listViewProfiles);
        deleteBtn = findViewById(R.id.deleteAccBtn);

        Intent intent = getIntent();

        profiles = new ArrayList<>();

        String id = intent.getStringExtra(AdminActivity.USER_ID);
        String name = intent.getStringExtra(AdminActivity.USER_NAME);

        profileDatabase = FirebaseDatabase.getInstance().getReference().child("Profiles");
       // updateAccount(id, textViewName, textViewSex, textViewBirthday);

        /*deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // deleteAccount(id);
            }
        });*/
    }

    /*private boolean updateAccount(String id, String username, String email, String password){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id);

            User user = new User(id, username, email, password);

            databaseReference.setValue(user);

            return true;

    }

    private void deleteAccount() {


    }*/
    //Intent intent = getIntent();
    //String id = intent.getStringExtra(AdminActivity.USER_ID);

    @Override
    protected void onStart() {
        super.onStart();

        profileDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 profiles.clear();

                 for(DataSnapshot profileSnapshot : dataSnapshot.getChildren()){
                   UserProfile profile = profileSnapshot.getValue(UserProfile.class);
                   profiles.add(profile);
                }

                ProfileList adapter = new ProfileList(AdminUpdate.this, profiles);
                listViewProfiles.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminUpdate.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
