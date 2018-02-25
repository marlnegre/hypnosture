package com.google.hangouts.hypnosture;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Exercise extends Fragment {



    ListView listOfExercises;
    public Fragment_Exercise() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__exercise, container, false);
        String[] exercises = {" UP DOWN EXERCISE", " HEAD SHAKE EXERCISE", " SIDE TILT EXERCISE", " ET EXERCISE"};
        Integer[] images = {R.drawable.updown, R.drawable.headshake, R.drawable.sidetilt, R.drawable.etexercise};

        listOfExercises = (ListView)view.findViewById(R.id.listView);

        ListViewClass adapter = new ListViewClass(Fragment_Exercise.this, exercises, images);
        listOfExercises.setAdapter(adapter);

        listOfExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent upDownExerciseIntent = new Intent (getActivity(), UpdownExerciseActivity.class);
                    startActivity(upDownExerciseIntent);
                }
                if (position == 1) {
                    Intent headShakeExerciseIntent = new Intent (getActivity(), headShakeActivity.class);
                    startActivity(headShakeExerciseIntent);
                }
                if (position == 2) {
                    Intent sideTiltExerciseIntent = new Intent (getActivity(), SideTiltExercise.class);
                    startActivity(sideTiltExerciseIntent);
                }
                if (position == 3) {
                    Intent ETExerciseIntent = new Intent (getActivity(), ETExercise.class);
                    startActivity(ETExerciseIntent);
                }
            }
        });
        return view;
    }


}
