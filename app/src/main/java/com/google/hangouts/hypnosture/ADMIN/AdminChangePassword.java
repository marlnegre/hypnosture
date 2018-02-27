package com.google.hangouts.hypnosture.ADMIN;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.hangouts.hypnosture.R;
import com.google.hangouts.hypnosture.USER.ChangingPassword;
import com.google.hangouts.hypnosture.USER.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class AdminChangePassword extends AppCompatActivity {

    EditText change;
    Button changeBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        backButton();

        mAuth = FirebaseAuth.getInstance();

        change = findViewById(R.id.changepassEditText);
        changeBtn = findViewById(R.id.changeBtn);
    }
    public void backButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void changePassword(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String password = change.getText().toString();

        if(password.isEmpty()){
            change.setError(" ");
            change.requestFocus();
            return;
        }

        if (password.length() < 6) {
            change.setError("Minimum length must be 6");
            change.requestFocus();
            return;
        }

        if(user!=null){
            user.updatePassword(change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_profile = FirebaseDatabase.getInstance().getReference().child("Admins").child(user_id);
                        Map newUserInfo = new HashMap();
                        newUserInfo.put("password", password);
                        current_user_profile.updateChildren(newUserInfo);
                        Toast.makeText(AdminChangePassword.this, "Your password has been changed!", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(AdminChangePassword.this, AdminLogin.class));
                    }
                    else{
                        Toast.makeText(AdminChangePassword.this, "Your password cannot be changed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}
