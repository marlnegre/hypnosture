package com.google.hangouts.hypnosture;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.CollationElementIterator;


public class EditProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    ImageView imageView;
    EditText fname, lname, bdate;
    Uri uriProfilePicture;
    String profilePictureurl;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.profileImage);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        bdate = findViewById(R.id.bdate);
        progressBar = findViewById(R.id.profileProgressbar);

        loadUserInfo();

        findViewById(R.id.profileImage).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        findViewById(R.id.okbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @SuppressLint("CheckResult")
    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if(user.getPhotoUrl()!= null){
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }
            if(user.getDisplayName() != null) {
                fname.setText(user.getDisplayName());
                lname.setText(user.getDisplayName());
            }

        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.maleRBtn:
                if (checked)
                    Toast.makeText(EditProfileActivity.this, "Male Updated!", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.femaleRBtn:
                if (checked)
                    Toast.makeText(EditProfileActivity.this, "Female Updated!", Toast.LENGTH_SHORT).show();
                    break;
        }
    }
    private void saveUserInformation() {
        String displayFName = fname.getText().toString();
        String displayLName = lname.getText().toString();
        String displayBDate = bdate.getText().toString();

        if(displayFName.isEmpty()){
            fname.setError("First Name is required");
            fname.requestFocus();
        }
        if(displayLName.isEmpty()){
            lname.setError("Last Name is required");
            lname.requestFocus();
        }
        if(displayBDate.isEmpty()){
            bdate.setError("First Name is required");
            bdate.requestFocus();
        }
        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null && profilePictureurl!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName("Full Name: " + fname + " " + lname + "\n" +
                                    "Birthdate: " + bdate)
                    .setPhotoUri(Uri.parse(profilePictureurl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                                Intent intent = (new Intent(EditProfileActivity.this, Calibrate.class));
                                startActivity(intent);
                            }
                        }
                    });
        }
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
             progressBar.setVisibility(View.VISIBLE );
             profilePicStorage.putFile(uriProfilePicture)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);

                            profilePictureurl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             progressBar.setVisibility(View.GONE);
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
