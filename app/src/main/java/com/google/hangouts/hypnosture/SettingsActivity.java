package com.google.hangouts.hypnosture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static SeekBar seekbar_posture, seekbar_snore;
    private static TextView ctr_posture, ctr_snore;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Changes saved", Toast.LENGTH_LONG).show();
            }
        });

        seekbarPosture();
        seekbarSnore();
        backButton();
    }

    public void seekbarPosture () {
        seekbar_posture = (SeekBar) findViewById(R.id.seekbar_posture);
        ctr_posture = (TextView) findViewById(R.id.secsPosture);

        seekbar_posture.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress_value = i;
                seekbar_posture.setMax(10);
                ctr_posture.setText(" "+ i +"s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ctr_posture.setText(" "+ progress_value + "s");
            }
        });

    }

    public void seekbarSnore () {
        seekbar_snore = (SeekBar) findViewById(R.id.seekbarSnore);
        ctr_snore = (TextView) findViewById(R.id.secsSnore);

        seekbar_snore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress_value = i;
                seekbar_snore.setMax(10);
                ctr_snore.setText(" "+ i +"s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ctr_snore.setText(" "+ progress_value + "s");
            }
        });
    }

    public void backButton ()  {
        Toolbar settingsToolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        settingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SettingsActivity.this, Homescreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}
