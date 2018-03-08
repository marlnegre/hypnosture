package com.google.hangouts.hypnosture;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.hangouts.hypnosture.model.statistics_helper.StatisticsHelper;

import java.util.Date;

public class PerformanceTrack extends AppCompatActivity {

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_performance_track);

        calendarView = (CalendarView) findViewById(R.id.calendar);

        Toolbar toolbar = findViewById(R.id.toolbar_performance);

        setSupportActionBar(toolbar);

        setBackBtn();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                AlertDialog.Builder builder = new AlertDialog.Builder(PerformanceTrack.this);
                final View view = getLayoutInflater().inflate(R.layout.dialogbox, null);
                TextView printDate = view.findViewById(R.id.dates);
                final TextView postureNumber = view.findViewById(R.id.postureNumber);
                final ProgressBar progressBar = view.findViewById(R.id.progressBar);

                StatisticsHelper.getStatistics(new Date(date), new StatisticsHelper.GetStatisticsCallback() {
                    @Override
                    public void onGet(Long improperPosture) {
                        progressBar.setVisibility(View.INVISIBLE);
                        postureNumber.setText(String.format(improperPosture.toString()));
                    }
                });

                printDate.setText(date.toString());
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();
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
