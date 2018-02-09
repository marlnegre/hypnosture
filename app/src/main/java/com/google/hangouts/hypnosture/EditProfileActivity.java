package com.google.hangouts.hypnosture;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class EditProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    ImageView imageView;
    EditText name, bdate;
    Uri uriProfilePicture;
    String profilePictureurl;
    RadioButton rb, rb1, rb2;
    RadioGroup rg;
    TextView textView;
    ProgressDialog dialog;

   // FirebaseDatabase database;
    DatabaseReference databaseProfile;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dialog = new ProgressDialog(EditProfileActivity.this);

        textView = findViewById(R.id.displayVerification);
        rb1 = findViewById(R.id.maleRB);
        rb2 =  findViewById(R.id.femaleRB);
        rg = findViewById(R.id.rg);
        mAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.name);
        bdate = findViewById(R.id.birthday);

      //  database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        String id = intent.getStringExtra(Signup_Screen.USER_ID);
        String name = intent.getStringExtra(Signup_Screen.USER_NAME);

        databaseProfile = FirebaseDatabase.getInstance().getReference("Profiles").child(id);

        loadUserInfo();

        findViewById(R.id.changebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, ChangingPassword.class));
            }
        });

        findViewById(R.id.profileImage).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        findViewById(R.id.okbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameExists();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void loadUserInfo() {
       final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if(user.getPhotoUrl()!= null){
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }
            if(user.getDisplayName() != null) {
                name.setText(user.getDisplayName());
            }
            if(user.isEmailVerified()){
                textView.setText("Email Verified");
            }
            else{
                textView.setText("Email Not Verified (Click to verify.)");

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditProfileActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }
    }

    public void rbclick(View v){

        int radioBtnID = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(radioBtnID);

    }


    private void saveUserInformation() {
        final String displayName = name.getText().toString();
        final String displayBDate = bdate.getText().toString();
        int radioBtnID = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(radioBtnID);
        final String radioText=rb.getText().toString();


        if(!rb1.isChecked() && !rb2.isChecked()){
            Toast.makeText(EditProfileActivity.this, "Select sex", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(displayName.isEmpty() || displayBDate.isEmpty())
        {
            Toast.makeText(EditProfileActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }

        else {
            final FirebaseUser user = mAuth.getCurrentUser();

            if (user != null && profilePictureurl != null) {
                final UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setDisplayName("asdasdas")
                        .setPhotoUri(Uri.parse(profilePictureurl))
                        .build();

                user.updateProfile(profile)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dialog.setMessage("Saving information....Please wait.");
                                    dialog.show();

                                    String id = databaseProfile.push().getKey();

                                    UserProfile newProfile = new UserProfile(id, displayName, radioText, displayBDate, profilePictureurl);

                                    databaseProfile.child(id).setValue(newProfile);


                                    Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = (new Intent(EditProfileActivity.this, Calibrate.class));
                                    startActivity(intent);
                                }
                            }
                        });
            }
        }
    }



    private void usernameExists(){

        String user_id_ = mAuth.getCurrentUser().getUid();

        Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").equalTo(user_id_);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Toast.makeText(EditProfileActivity.this, "Choose a different username.", Toast.LENGTH_SHORT).show();
                } else {
                    saveUserInformation();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            uriProfilePicture = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfilePicture);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profilePicStorage =
                FirebaseStorage.getInstance().getReference("profilepics/" +
                        System.currentTimeMillis() + ".jpg");

        if(uriProfilePicture != null){
            profilePicStorage.putFile(uriProfilePicture)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profilePictureurl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);
    }

}