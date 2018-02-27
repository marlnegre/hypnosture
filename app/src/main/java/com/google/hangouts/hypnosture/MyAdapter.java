package com.google.hangouts.hypnosture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by christian Kent Igot on 27/02/2018.
 */

public class MyAdapter extends ArrayAdapter<String> {

    String [] recommendations;
    int [] images;
    Context context;

    public MyAdapter(Context context, String[] recommendations, int[] images){
        super(context, R.layout.activity_listview1);

        this.recommendations = recommendations;
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recommendations.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewholder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_listview1, parent, false);

            mViewholder.images = (ImageView) convertView.findViewById(R.id.recomimg);
            mViewholder.recommendations = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(mViewholder);
        }else {
            mViewholder = (ViewHolder) convertView.getTag();
        }
            mViewholder.images.setImageResource(images[position]);
            mViewholder.recommendations.setText(recommendations[position]);

        return convertView;
    }
    static class ViewHolder{
        ImageView images;
        TextView recommendations;
    }
}
