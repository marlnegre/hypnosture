package com.google.hangouts.hypnosture;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.hangouts.hypnosture.USER.UserProfile;


/**
 * A simple {@link Fragment} subclass.
 */
public class Recommendation extends Fragment {

    ImageButton postureRec, recExercise;

    public Recommendation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

            postureRec = (ImageButton) view.findViewById(R.id.postureRecommendation);
            recExercise = (ImageButton) view.findViewById(R.id.recommendedExercise);

            postureRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecommendationActivity.class);
                startActivity(intent);
            }
        });
             recExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ExercisesActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
