package com.google.hangouts.hypnosture;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Week_Four extends Fragment {
    View view;
    BarChart mChart;

    public Week_Four() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_week__four, container, false);

        mChart = view.findViewById(R.id.weekFour);
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

        return view;
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
    public  class MyAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;
        public MyAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];

        }
    }


}
