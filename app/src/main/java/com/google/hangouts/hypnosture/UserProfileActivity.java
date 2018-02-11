package com.google.hangouts.hypnosture;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {


    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;

    TextView textView;
    ImageView userImageProfileview;
    EditText name, birthday;
    RadioGroup rg;
    RadioButton rb, rb1, rb2;
    Button okbutton;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mUserDatabase;
    StorageReference mStorageref;

    Uri imageHoldUri = null;

    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textView = findViewById(R.id.displayVerification);
        userImageProfileview = findViewById(R.id.profileImage);
        name = findViewById(R.id.name);
        birthday = findViewById(R.id.birthday);
        rb1 = findViewById(R.id.maleRB);
        rb2 =  findViewById(R.id.femaleRB);
        rg = findViewById(R.id.rg);
        okbutton = findViewById(R.id.okbutton);


        Intent intent = getIntent();
        final String id = intent.getStringExtra(Signup_Screen.USER_ID);
        String name = intent.getStringExtra(Signup_Screen.USER_NAME);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null){
                    finish();
                    startActivity(new Intent(UserProfileActivity.this, Homescreen.class));
                }
            }
        };


        mProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageref = FirebaseStorage.getInstance().getReference();

        loadUserInfo();

        okbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

               saveUserProfile();

            }

        });

        userImageProfileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePicSelection();
            }
        });

    }
   @SuppressLint("CheckResult")
   private void loadUserInfo() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
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
                                Toast.makeText(UserProfileActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
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


    private void saveUserProfile() {

        final String Username = name.getText().toString();
        final String Birthday = birthday.getText().toString();
        int radioBtnID = rg.getCheckedRadioButtonId();
        rb =  findViewById(radioBtnID);
        final String radioText= rb.getText().toString();


        if(!TextUtils.isEmpty(Username) && !TextUtils.isEmpty(Birthday))
        {
            if (userImageProfileview != null){

                mProgress.setTitle("Saving Profile");
                mProgress.setMessage("Please wait...");
                mProgress.show();

                StorageReference mChildStorage = mStorageref.child("User_Profile").child(imageHoldUri.getLastPathSegment());
                String profilePicUrl = imageHoldUri.getLastPathSegment();

                mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        final Uri imageUri = taskSnapshot.getDownloadUrl();
                        Intent intent = getIntent();
                        final String id = intent.getStringExtra(Signup_Screen.USER_ID);
                        String profilePhotoUrl = imageUri.toString();

                        //Map<String, Object> updateUserData = new HashMap<>();

                        UserProfile newProfile = new UserProfile(Username, radioText, Birthday, profilePhotoUrl);
                        mUserDatabase.child(id).setValue(newProfile);
                        //mUserDatabase.updateChildren(updateUserData);
                        Toast.makeText(UserProfileActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                });

            }else{
                Toast.makeText(this, "Please select profile picture!", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void profilePicSelection() {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
        "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Add Photo!");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")){
                    cameraIntent();
                }else if (items[item].equals("Choose from Gallery")){
                    galleryIntent();
                }else if (items[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
            builder.show();
    }

    private void cameraIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    private void galleryIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                imageHoldUri = result.getUri();

                userImageProfileview.setImageURI(imageHoldUri);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }

        }


    }
}
