package com.google.hangouts.hypnosture;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ExercisesActivity extends AppCompatActivity {

    String[] exercises = {"UP DOWN EXERCISE", "HEAD SHAKE EXERCISE", "SIDE TILT EXERCISE", "ET EXERCISE"};
    Integer[] images = {R.drawable.updown, R.drawable.headshake, R.drawable.sidetilt, R.drawable.etexercise};

    ListView listOfExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        setBackBtn();

        listOfExercises = findViewById(R.id.listView);

        //ListViewClass adapter = new ListViewClass(ExercisesActivity.this, exercises, images);
        //listOfExercises.setAdapter(adapter);
        listOfExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent upDownExerciseIntent = new Intent (ExercisesActivity.this, UpdownExerciseActivity.class);
                    startActivity(upDownExerciseIntent);
                }
                if (position == 1) {
                    Intent headShakeExerciseIntent = new Intent (ExercisesActivity.this, headShakeActivity.class);
                    startActivity(headShakeExerciseIntent);
                }
                if (position == 2) {
                    Intent sideTiltExerciseIntent = new Intent (ExercisesActivity.this, SideTiltExercise.class);
                    startActivity(sideTiltExerciseIntent);
                }
                if (position == 3) {
                    Intent ETExerciseIntent = new Intent (ExercisesActivity.this, ETExercise.class);
                    startActivity(ETExerciseIntent);
                }
            }
        });
    }


    public void setBackBtn() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}