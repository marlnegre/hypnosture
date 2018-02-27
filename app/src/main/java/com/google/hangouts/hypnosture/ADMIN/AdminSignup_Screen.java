package com.google.hangouts.hypnosture.ADMIN;

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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.hangouts.hypnosture.Activity_Homescreen;
import com.google.hangouts.hypnosture.R;
import com.google.hangouts.hypnosture.USER.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminSignup_Screen extends AppCompatActivity {

    private static final String TAG = "Signup_Screen";
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    public static final String USER_ID = "artistid";
    public static final String USER_NAME = "artistname";

    Button signup;
    EditText email, password, confirm;
    private FirebaseAuth mAuth;
    private TextView textViewBirthdate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ProgressDialog mProgress;

    DatabaseReference databaseUsers;
    FirebaseDatabase usersDatabase;
    StorageReference mStorageref;

    CircleImageView userImageProfileview;
    EditText fname;
    RadioGroup rg;
    RadioButton rb, rb1, rb2;
    FirebaseAuth.AuthStateListener mAuthListener;
    Uri imageHoldUri = null;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup__screen);


        mProgress = new ProgressDialog(this);
        textViewBirthdate = findViewById(R.id.textViewBirthday);
        databaseUsers = FirebaseDatabase.getInstance().getReference("Admins");
        usersDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText5);
        password = findViewById(R.id.editText6);
        confirm = findViewById(R.id.editText3);
        signup = findViewById(R.id.submitSignup);
        userImageProfileview = findViewById(R.id.profileImage);
        fname = findViewById(R.id.fname);
        rb1 = findViewById(R.id.maleRB);
        rb2 =  findViewById(R.id.femaleRB);
        rg = findViewById(R.id.rg);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mStorageref = FirebaseStorage.getInstance().getReference();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    finish();
                    startActivity(new Intent(AdminSignup_Screen.this, Activity_Homescreen.class));
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
                        AdminSignup_Screen.this,
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

        userImageProfileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePicSelection();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                final String Confirm = confirm.getText().toString().trim();
                final String Fname = fname.getText().toString();
                final String Birthday = textViewBirthdate.getText().toString();
                int radioBtnID = rg.getCheckedRadioButtonId();

                rb =  findViewById(radioBtnID);

                String edit_text_fname = Fname;

                Pattern regex = Pattern.compile("[$&+,:;=?@#|/'<>. ^*()%-]");
                Pattern regexx = Pattern.compile("[$&+,:;=?@#|/'<>.^*0123456789()%-]");


                if (Email.isEmpty() || Password.isEmpty() || Confirm.isEmpty() || TextUtils.isEmpty(Fname) || TextUtils.isEmpty(Birthday)){
                    Toast.makeText(AdminSignup_Screen.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (regexx.matcher(edit_text_fname).find()) {
                    fname.setError("Must not contain [0-9] and [$&+,:;=?@#|/'<>. ^*()%-]");
                    fname.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }
                if (Password.length() < 6) {
                    password.setError("Minimum length must be 6");
                    password.requestFocus();
                    return;
                }
                if (!Confirm.equals(Password)) {
                    confirm.setError("Must match with the inputted previous password");
                    confirm.requestFocus();
                    return;
                }


                if (userImageProfileview.getDrawable() == null){
                    Toast.makeText(AdminSignup_Screen.this, "Select Profile Picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!rb1.isChecked() && !rb2.isChecked()) {
                    Toast.makeText(AdminSignup_Screen.this, "Select Sex", Toast.LENGTH_SHORT).show();
                    return;
                }

                    /*Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(User);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                Toast.makeText(Signup_Screen.this, "Choose a different username.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                registerUser();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });*/

                registerUser();

            }
        });
    }



    public void registerUser() {

        final String Email = email.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Fname = fname.getText().toString();
        final String Birthday = textViewBirthdate.getText().toString();
        final int radioBtnID = rg.getCheckedRadioButtonId();

        rb =  findViewById(radioBtnID);

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()) {
                            mProgress.setTitle("Registering User");
                            mProgress.setMessage("Please wait...");
                            mProgress.show();

                            StorageReference mChildStorage = mStorageref.child("profilepics").child(imageHoldUri.getLastPathSegment());
                            final String profilePicUrl = imageHoldUri.getLastPathSegment();

                            mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Uri imageUri = taskSnapshot.getDownloadUrl();
                                    String profilePhotoUrl = imageUri.toString();


                                    FirebaseUser user = mAuth.getCurrentUser();
                                    userID = user.getUid();

                                    User newUser = new User(userID, Email, Password, Fname, rb.getText().toString(), Birthday, profilePhotoUrl);
                                    databaseUsers.child(userID).setValue(newUser);
                                    Intent profileintent = new Intent(AdminSignup_Screen.this, UsersActivity.class);
                                    startActivity(profileintent);
                                }
                            });
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



    public void rbclick(View v){

        int radioBtnID = rg.getCheckedRadioButtonId();
        rb = findViewById(radioBtnID);

    }


    private void profilePicSelection() {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminSignup_Screen.this);
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
