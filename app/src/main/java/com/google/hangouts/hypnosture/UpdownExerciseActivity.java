package com.google.hangouts.hypnosture;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class UpdownExerciseActivity extends AppCompatActivity {

    private Button startButton;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updown_exercise);

        timer = (TextView) findViewById(R.id.updownTimer);
        startButton = (Button) findViewById(R.id.sbutton_UPDown);

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
                CountDownTimer firstlap = new CountDownTimer(20000, 1000) {
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
                                CountDownTimer secondlap = new CountDownTimer(20000, 1000) {
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
                                                                Intent menu = new Intent(UpdownExerciseActivity.this, ExercisesActivity.class);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(UpdownExerciseActivity.this);
                builder.setTitle(R.string.information);
                builder.setMessage("PROBLEM: Incorrect head posture deteriorates neck movement functions\n" +
                        "to strain muscles and joints. In results, only limited parts are used." +
                        "\n\nSOLUTION: Relax and stretch the upper trapezius muscle and levator scapulae." +
                        "\n\nEXERCISE: While trying not to pull your chin too high, pull your chin\n" +
                        "slightly with your fist and gently tilt your head back and forth repeatedly.");
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
        Toolbar updownExerciseToolbar = (Toolbar) findViewById(R.id.updownExercisetoolbar);
        setSupportActionBar(updownExerciseToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        updownExerciseToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(UpdownExerciseActivity.this, Fragment_Exercise.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fragment, new Fragment_Exercise()).commit();
            finish();
            }
        });
    }
}
