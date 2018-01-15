package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int CHOOSE_IMAGE = 101;
    ImageView imageView;
    EditText fname, lname, bdate;
    Uri uriProfilePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViewById(R.id.profileImage).setOnClickListener(this);

        refs();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfilePicture = data.getData();
        }
    }

    public void refs(){
        imageView = findViewById(R.id.profileImage);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        bdate = findViewById(R.id.bdate);
    }

    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivity(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);
    }
    @Override
    public void onClick(View v) {

    }
}
