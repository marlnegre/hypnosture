package com.google.hangouts.hypnosture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Gallery extends Fragment {

    GridView gridview;
    List<String> your_array_list = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    public static class ImageAdapter extends BaseAdapter {
        String [] item;
        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path){
            itemList.add(path);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(120, 220));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 120, 120);

            imageView.setImageBitmap(bm);
            return imageView;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

            Bitmap bm = null;

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);


            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {

            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float)height / (float)reqHeight);
                } else {
                    inSampleSize = Math.round((float)width / (float)reqWidth);
                }
            }

            return inSampleSize;
        }
    }

    ImageAdapter myImageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myImageAdapter = new ImageAdapter(getActivity());

        PhotoManager sm=new PhotoManager();
        ArrayList<String> images = sm.getPlayList();
        if(images.isEmpty()){
            Toast.makeText(getActivity(),"No images found",Toast.LENGTH_SHORT).show();
        }
        else {
            for (String image : images) {
                myImageAdapter.add(image);

            }
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridview = view.findViewById(R.id.gridview);
        gridview.setAdapter(myImageAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__gallery, container, false);
    }


}
