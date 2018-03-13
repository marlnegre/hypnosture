package com.google.hangouts.hypnosture;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.hangouts.hypnosture.model.snore_helper.SnoreHelper;
import com.google.hangouts.hypnosture.model.statistics_helper.StatisticsHelper;

import java.util.Date;

public class SnoreTrackActivity extends AppCompatActivity {

    CalendarView calendarView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_snoretrack);

        Toolbar toolbar = findViewById(R.id.toolbar_snore);
        setSupportActionBar(toolbar);

        setBackBtn();

        calendarView = findViewById(R.id.calendar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                AlertDialog.Builder builder = new AlertDialog.Builder(SnoreTrackActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialogsnore, null);
                TextView printDate = view.findViewById(R.id.dates);
                final TextView snoreNumber = view.findViewById(R.id.snoreNumber);
                final ProgressBar progressBar = view.findViewById(R.id.progressBar);

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;

                if(connected) {
                    SnoreHelper.getSnore(new Date(date), new SnoreHelper.GetSnoreCallback() {
                        @Override
                        public void onGet(Long snoreDetected) {
                            progressBar.setVisibility(View.INVISIBLE);
                            snoreNumber.setText(String.format(snoreDetected.toString()));
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
                else
                    Toast.makeText(getApplication(),"No Internet Connection", Toast.LENGTH_SHORT).show();
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
