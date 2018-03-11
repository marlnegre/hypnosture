package com.google.hangouts.hypnosture;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {

    ImageView image;
    TextView desc,date,title;
    Button recomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        image = (ImageView) findViewById(R.id.imageView4);
        desc = (TextView) findViewById(R.id.improper_desc);
        date = (TextView) findViewById(R.id.dateTime);
        recomBtn = (Button) findViewById(R.id.recom);
        title = (TextView) findViewById(R.id.titleView);

        String file = getIntent().getStringExtra("img");
        String filename = file.substring(file.lastIndexOf("/"));

        StringBuilder sb = new StringBuilder(filename);
        int length = sb.length();
        sb.deleteCharAt(0);
        sb.delete(length-20, length);

        image.setImageURI(Uri.parse(file));
        date.setText(sb);
        recomBtn.setOnClickListener(recommend);
    }
    private View.OnClickListener recommend = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, new Recommendation()).commit();
            image.setVisibility(View.INVISIBLE);
            desc.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
            recomBtn.setVisibility(View.INVISIBLE);
            title.setText("Recommendations");
        }
    };

}
