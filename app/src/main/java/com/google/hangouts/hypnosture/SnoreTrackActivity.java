package com.google.hangouts.hypnosture;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SnoreTrackActivity extends AppCompatActivity {

    ImageButton sun;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snoretrack);

        setupPieChart();
    }

    private void setupPieChart()
    {
       PieChart pieChart = (PieChart) findViewById(R.id.chart);

       pieChart.setUsePercentValues(true);
       pieChart.getDescription().setEnabled(false);
       pieChart.setExtraOffsets(5,10,5,5);

       pieChart.setDragDecelerationFrictionCoef(8.95f);

       pieChart.setDrawHoleEnabled(true);
       pieChart.setHoleColor(Color.WHITE);
       pieChart.setTransparentCircleRadius(61f);

       ArrayList<PieEntry> yValues = new ArrayList<>();
            yValues.add(new PieEntry(34f, "LOUD"));
            yValues.add(new PieEntry(50f, "EPIC"));
            yValues.add(new PieEntry(25f, "MILD"));


        PieDataSet dataSet = new PieDataSet(yValues, "");

        dataSet.setLabel("LEGEND");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);


        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("LEGEND");
        labels.add("NO. OF TIMES SNORED");

        pieChart.setRotationEnabled(false);
        pieChart.setData(data);
        pieChart.animateY(2000);
        pieChart.setCenterText("1hr 45 min");

        pieChart.invalidate();
        dataSet.setSliceSpace(3);
       // dataSet.setSelectionShift(5);


    }
}
