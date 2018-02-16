package com.google.hangouts.hypnosture;

import android.service.autofill.Dataset;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    private BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        mChart = findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(true);

        setData(7);
        mChart.setFitBars(true);



        ArrayList xVals = new ArrayList();

        xVals.add("03/01/18");
        xVals.add("03/02/18");
        xVals.add("03/03/18");
        xVals.add("03/04/18");
        xVals.add("03/05/18");
        xVals.add("03/06/18");
        xVals.add("03/07/18");
    }

    private void setData(int count) {
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for (int i = 0; i < count; i++){
            float value = (float) (Math.random()*100);
            yVals.add(new BarEntry(i, (int) value));
        }

        BarDataSet set = new BarDataSet(yVals, "");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);
        Legend legend = mChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        mChart.setData(data);
        mChart.invalidate();
        mChart.animateY(500);
        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setText(" ");
        mChart.getAxisRight().setDrawLabels(false);
    }
}
