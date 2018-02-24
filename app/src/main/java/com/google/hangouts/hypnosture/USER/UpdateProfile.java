package com.google.hangouts.hypnosture.USER;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.hangouts.hypnosture.Activity_Homescreen;
import com.google.hangouts.hypnosture.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;

    CircleImageView userImageProfileview;
    EditText fullname;
    RadioGroup rg;
    RadioButton rb, rb1, rb2;
    Button ok;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textViewBirthdate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    DatabaseReference mUserDatabase;
    StorageReference mStorageref;

    Uri imageHoldUri = null;

    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        userImageProfileview = findViewById(R.id.profileImage);
        fullname = findViewById(R.id.fname);
        textViewBirthdate = findViewById(R.id.textViewBirthday);
        rb1 = findViewById(R.id.maleRB);
        rb2 =  findViewById(R.id.femaleRB);
        rg = findViewById(R.id.rg);
        ok = findViewById(R.id.okbutton);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null){
                    finish();
                    startActivity(new Intent(UpdateProfile.this, Activity_Homescreen.class));
                }
            }
        };

        textViewBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UpdateProfile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                final String Birthday = month + "/" + dayOfMonth + "/" + year;
                textViewBirthdate.setText(Birthday);
            }
        };

        mProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageref = FirebaseStorage.getInstance().getReference();

        ok.setOnClickListener(new View.OnClickListener(){
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


    public void rbclick(View v){

        int radioBtnID = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(radioBtnID);

    }

    private void saveUserProfile() {

        final String Fname = fullname.getText().toString();
        int radioBtnID = rg.getCheckedRadioButtonId();
        final String Birthday = textViewBirthdate.getText().toString();
        rb =  findViewById(radioBtnID);

        String edit_text_fname = Fname;

        Pattern regexx = Pattern.compile("[$&+,:;=?@#|/'<>.^*0123456789()%-]");


        if (TextUtils.isEmpty(Fname)  || TextUtils.isEmpty(Birthday)){
            Toast.makeText(UpdateProfile.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (regexx.matcher(edit_text_fname).find()) {
            fullname.setError("Must not contain [0-9] and [$&+,:;=?@#|/'<>. ^*()%-]");
            fullname.requestFocus();
            return;
        }

        if (userImageProfileview.getDrawable() == null){
            Toast.makeText(UpdateProfile.this, "Select Profile Picture", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!rb1.isChecked() && !rb2.isChecked()) {
            Toast.makeText(UpdateProfile.this, "Select Sex", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null) {
            StorageReference mChildStorage = mStorageref.child("User_Profile").child(imageHoldUri.getLastPathSegment());
            String profilePicUrl = imageHoldUri.getLastPathSegment();

            mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri imageUri = taskSnapshot.getDownloadUrl();
                    String profilePhotoUrl = imageUri.toString();


                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_profile = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                    Map newUserInfo = new HashMap();
                    newUserInfo.put("fname", Fname);
                    newUserInfo.put("sex", rb.getText().toString());
                    newUserInfo.put("birthday", Birthday);
                    newUserInfo.put("profilePicURL", profilePhotoUrl);
                    current_user_profile.updateChildren(newUserInfo);




                    Toast.makeText(UpdateProfile.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();

                }
            });

        }
    }

    private void profilePicSelection() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
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
