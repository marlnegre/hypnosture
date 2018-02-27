package com.google.hangouts.hypnosture;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    TextView textView ;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar_);
        imageView = (ImageView) findViewById(R.id.imageView3);
        textView = (TextView) findViewById(R.id.info);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            toolbar.setTitle(bundle.getString("exercise"));
            imageView.setImageResource(bundle.getInt("image"));
            textView.setText(bundle.getString("info"));

        }
    }
}
