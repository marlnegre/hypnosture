package com.google.hangouts.hypnosture;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class ViewActivity extends AppCompatActivity {

    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        image = (ImageView) findViewById(R.id.imageView4);
        String f = getIntent().getStringExtra("img");


        image.setImageURI(Uri.parse(f));
    }
}
