package com.google.hangouts.hypnosture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by christian Kent Igot on 24/02/2018.
 */

public class Fragament_HomeReflect extends Fragment {

    Button disconnect;
    TextView display_angle;
    ImageView angle_image;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__homereflect, container, false);

//        disconnect = view.findViewById(R.id.btnDisconnect);
//        display_angle = view.findViewById(R.id.angle);
//        angle_image = view.findViewById(R.id.imageView2);

//        disconnect.setVisibility(view.GONE);

        return view;
    }


}
