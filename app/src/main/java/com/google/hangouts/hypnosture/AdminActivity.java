package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AdminActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword, editTextEmail;
    Button createUser;
    private FirebaseAuth mAuth;

    DatabaseReference databaseUsers;

    ListView listViewUsers;
    List<User> userList;
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        listViewUsers = findViewById(R.id.listViewUsers);
        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        createUser = findViewById(R.id.submitSignup);
        userList = new ArrayList<>();

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUser();
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);

                Intent intent = new Intent(AdminActivity.this, AdminUpdate.class);

                intent.putExtra(USER_ID, user.getUserId());
                intent.putExtra(USER_NAME, user.getUsername());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User profiles = userSnapshot.getValue(User.class);

                    userList.add(profiles);
                }

                UserList adapter = new UserList(AdminActivity.this, userList);
                listViewUsers.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void AddUser(){
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String email = editTextEmail.getText().toString();

        String edit_text_name = username;

        Pattern regex = Pattern.compile("[$&+,:;=?@#|/'<>. ^*()%-]");

        if (regex.matcher(edit_text_name).find()) {
            Toast.makeText(AdminActivity.this, "Must not contain the following [$&+,:;=?@#|/'<>. ^*()%-]", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(AdminActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Minimum length must be 6");
            editTextPassword.requestFocus();
            return;
        }


        Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(username);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Toast.makeText(AdminActivity.this, "Choose a different username.", Toast.LENGTH_SHORT).show();
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


    public void registerUser() {

        final String User = editTextUsername.getText().toString().trim();
        final String Email = editTextEmail.getText().toString().trim();
        final String Password = editTextPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String id = databaseUsers.push().getKey();

                            User newUser = new User(id, User, Email, Password);

                            databaseUsers.child(id).setValue(newUser);

                            Toast.makeText(AdminActivity.this, "User added!", Toast.LENGTH_SHORT).show();

                            editTextUsername.setText("");
                            editTextPassword.setText("");
                            editTextEmail.setText("");
                            editTextUsername.requestFocus();
                            Intent intent = new Intent();
                            intent.putExtra(USER_ID, newUser.getUserId());
                            intent.putExtra(USER_NAME, newUser.getUsername());
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
