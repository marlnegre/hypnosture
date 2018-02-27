package com.google.hangouts.hypnosture;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class headShakeActivity extends AppCompatActivity {

    private Button startButton;
    private TextView timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_shake);

        timer = (TextView) findViewById(R.id.headShakeTimer);
        startButton = (Button) findViewById(R.id.startButton);

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
                                                                Intent menu = new Intent(headShakeActivity.this, ExercisesActivity.class);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(headShakeActivity.this);
                builder.setTitle(R.string.information);
                builder.setMessage("PROBLEM: Excessive tensions on neck muscles put pressure on joints\n" +
                        " more likely to cause pain and blood-flow disorders." +
                        "\n\nSOLUTION: Try relax your neck with deep breath to loose tension and\n" +
                        " stretch the sternocleidomastoid muscle, splenius muscle and\n" +
                        " suboccpital musclesfor head rotation." +
                        "\n\nEXERCISE: Correct your neck posture and relieve muscles as much as you can.\n" +
                        "Rotate your head left and right about 1/3 of a circle.");
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
        Toolbar headShakeExerciseToolbar = (Toolbar) findViewById(R.id.headShakeToolbar);
        setSupportActionBar(headShakeExerciseToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headShakeExerciseToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(headShakeActivity.this, ExercisesActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
                finish();
            }
        });
    }
}
