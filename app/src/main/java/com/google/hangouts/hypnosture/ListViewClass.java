package com.google.hangouts.hypnosture;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewClass extends ArrayAdapter<String> {

    private final Activity context;
    private final String [] exercises;
    private final Integer[] images;

    public ListViewClass (Activity context, String[] exercises, Integer[] images)
    {
        super (context, R.layout.activity_listview, exercises);
        this.context = context;
        this.exercises = exercises;
        this.images = images;
    }


    public View getView (int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();

        View row = inflater.inflate(R.layout.activity_listview,null,true);
        TextView text = (TextView) row.findViewById(R.id.textView);

        ImageView imgView = (ImageView) row.findViewById(R.id.exerciseImg);
        text.setText(exercises[position]);

        imgView.setImageResource(images[position]);
        return row;
    }

}