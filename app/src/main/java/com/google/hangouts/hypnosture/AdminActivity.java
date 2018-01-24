package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int CHOOSE_IMAGE = 1;
    Uri uriProfilePicture;
    Button view_btn, update_btn, delete_btn, create_btn;
    ImageView adminProfileBtn;
    String profilePictureurl;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findViewById(R.id.AdminprofileBtn).setOnClickListener(this);
        findViewById(R.id.viewBtn).setOnClickListener(this);
        findViewById(R.id.updateBtn).setOnClickListener(this);
        findViewById(R.id.deleteBtn).setOnClickListener(this);
        findViewById(R.id.createBtn).setOnClickListener(this);

        chooseImage();
        refs();
    }

    public void createUser(){

        Intent intent = new Intent(AdminActivity.this, Signup_Screen.class);
        startActivity(intent);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            uriProfilePicture = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfilePicture);
                adminProfileBtn.setImageBitmap(bitmap);

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
                            Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void refs(){
        adminProfileBtn = findViewById(R.id.AdminprofileBtn);
        view_btn = findViewById(R.id.viewBtn);
        update_btn = findViewById(R.id.updateBtn);
        delete_btn = findViewById(R.id.deleteBtn);
        create_btn = findViewById(R.id.createBtn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewBtn:

                break;

            case R.id.updateBtn:

                break;

            case R.id.deleteBtn:

                break;

            case R.id.createBtn:
                createUser();
                break;
        }
    }
}
