package com.google.hangouts.hypnosture;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SideTiltExercise extends AppCompatActivity {


    private Button startButton;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_tilt_exercise);

        timer = (TextView) findViewById(R.id.sideTiltTimer);
        startButton = (Button) findViewById(R.id.sbutton_sideTilt);

        laps();
        informationButton();
        backButton();
    }

    public void laps ()
    {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startButton.setVisibility(View.INVISIBLE);
                CountDownTimer firstlap = new CountDownTimer(20001, 1000) {
                    @Override
                    public void onTick(long l) {
                        timer.setText(" " + l / 1000);
                    }

                    @Override
                    public void onFinish() {
                        timer.setText("FIRST LAP DONE");
                        startButton.setVisibility(View.VISIBLE);
                        startButton.setText("NEXT");

                        startButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startButton.setVisibility(View.INVISIBLE);
                                CountDownTimer secondlap = new CountDownTimer(20001, 1000) {
                                    @Override
                                    public void onTick(long l) {  timer.setText(" " + l / 1000); }

                                    @Override
                                    public void onFinish() {
                                        timer.setText("SECOND LAP DONE");
                                        startButton.setVisibility(View.VISIBLE);
                                        startButton.setText("NEXT");

                                        startButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startButton.setVisibility(View.INVISIBLE);
                                                CountDownTimer thirdlap = new CountDownTimer(20000, 1000) {
                                                    @Override
                                                    public void onTick(long l) {  timer.setText(" " + l / 1000); }

                                                    @Override
                                                    public void onFinish() {
                                                        timer.setText("MISSION ACCOMPLISHED");
                                                        startButton.setVisibility(View.VISIBLE);
                                                        startButton.setText("DONE");

                                                        startButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Intent menu = new Intent(SideTiltExercise.this, ExercisesActivity.class);
                                                                startActivity(menu);
                                                            }
                                                        });
                                                    }
                                                }.start();
                                            }
                                        });
                                    }
                                }.start();
                            }
                        });
                    }
                }.start();
            }
        });
    }

    public void informationButton  () {
        ImageButton info_icon = (ImageButton) findViewById(R.id.info_imgButton);

        info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SideTiltExercise.this);
                builder.setTitle(R.string.information);
                builder.setMessage("PROBLEM: Slouching posture shortens muscle length and\n" +
                        "increase tensions over time to harm your neck’s health conditions." +
                        "\n\nSOLUTION: Square your shoulders." +
                        "\n\nEXERCISE: Stretch your both arms down diagonally and pull your head up long.\n" +
                        "Exercise and stretch your shoulder muscles left and right.");
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public void backButton ()  {
        Toolbar sideTiltExerciseToolbar = (Toolbar) findViewById(R.id.sideTiltToolbar);
        setSupportActionBar(sideTiltExerciseToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sideTiltExerciseToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SideTiltExercise.this, ExercisesActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}
