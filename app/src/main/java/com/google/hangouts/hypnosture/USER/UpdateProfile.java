package com.google.hangouts.hypnosture.USER;

        import android.app.ProgressDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.hangouts.hypnosture.R;

        import java.util.HashMap;
        import java.util.Map;
        import java.util.regex.Pattern;

public class UpdateProfile extends AppCompatActivity{

    EditText changeFname, changeLname;
    Button changeBtn;
    FirebaseAuth mAuth;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgress = new ProgressDialog(this);

        backButton();

        mAuth = FirebaseAuth.getInstance();

        changeFname = findViewById(R.id.changeFirstName);
        changeLname = findViewById(R.id.changeLastName);
        changeBtn = findViewById(R.id.changeBtn);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileName();
            }
        });
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

    private void updateProfileName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String firstname = changeFname.getText().toString();
        String lastname = changeLname.getText().toString();

        Pattern regex = Pattern.compile("^[a-zA-Z]+$");

        if(firstname.isEmpty()){
            changeFname.setError("First Name is empty");
            changeFname.requestFocus();
            return;
        }

        if(lastname.isEmpty()){
            changeLname.setError("Last Name is empty");
            changeLname.requestFocus();
            return;
        }

        if (!(regex.matcher(firstname).find())) {
            changeFname.setError("Must contain alphabets only");
            changeFname.requestFocus();
            return;
        }

        if (!(regex.matcher(lastname).find())) {
            changeLname.setError("Must contain alphabets only");
            changeLname.requestFocus();
            return;
        }



        if(user!=null) {
            mProgress.setTitle("Updating Profile");
            mProgress.setMessage("Please wait...");
            mProgress.show();

            String user_id = mAuth.getCurrentUser().getUid();
            DatabaseReference current_user_profile = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
            Map newUserInfo = new HashMap();
            newUserInfo.put("fname", firstname);
            newUserInfo.put("lname", lastname);

            current_user_profile.updateChildren(newUserInfo);
            Toast.makeText(UpdateProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }
                    else {
            Toast.makeText(UpdateProfile.this, "Your profile cannot be updated!", Toast.LENGTH_SHORT).show();
        }
    }

}
