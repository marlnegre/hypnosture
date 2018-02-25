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


public class ETExercise extends AppCompatActivity {

    private Button startButton;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etexercise);

        timer = (TextView) findViewById(R.id.etTimer);
        startButton = (Button) findViewById(R.id.sButton_et);

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
                                                                Intent menu = new Intent(ETExercise.this, ExercisesActivity.class);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(ETExercise.this);
                builder.setTitle(R.string.information);
                builder.setMessage("PROBLEM: Forward head posture weakens muscles to straight up your\n" +
                        "neck and back but strengthens muscles to move your head forward.\n" +
                        "Such muscle unbalance makes more difficult to keep right posture." +
                        "\n\nSOLUTION: Balance should be recovered by exercising the opposite movements\n" +
                        " of the forward head posture. Strengthen your front deep neck flexor." +
                        "\n\nEXERCISE: Push your head front and pull your head back in straight\n" +
                        "trying not to tilt your chin.");
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
        Toolbar ETExerciseToolbar = (Toolbar) findViewById(R.id.ETExerciseToolbar);
        setSupportActionBar(ETExerciseToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ETExerciseToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(ETExercise.this, ExercisesActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
                finish();
            }
        });
    }
}
