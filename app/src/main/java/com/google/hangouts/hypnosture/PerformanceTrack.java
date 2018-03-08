package com.google.hangouts.hypnosture;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

public class PerformanceTrack extends AppCompatActivity {

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_track);

        calendarView = (CalendarView) findViewById(R.id.calendar);




        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
            String date = (i1 + 1) + "/" + i2 + "/" + i;
                 AlertDialog.Builder builder = new AlertDialog.Builder(PerformanceTrack.this);
                final View view = getLayoutInflater().inflate(R.layout.dialogbox, null);
                TextView printDate = view.findViewById(R.id.dates);
                TextView postureNumber = view.findViewById(R.id.postureNumber);

                printDate.setText(date.toString());
//                builder.setTitle(R.string.information);
//                builder.setMessage(date+"\n\n2");
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
}
