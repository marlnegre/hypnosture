package com.google.hangouts.hypnosture;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Week_one extends Fragment {
    View view;
    BarChart mChart;
    BarData barData;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;
    private static final String TAG = "Week_one";

    public Week_one() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_week_one, container, false);

        mChart = view.findViewById(R.id.weekOne);
        mChart.getDescription().setEnabled(true);

        setData(7);
        mChart.setFitBars(true);

        //createRandomBarGraph("2016/05/05" , "2016/05/12");

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

//    private void setData(int count) {
//        createRandomBarGraph("2016/06/05" , "2016/06/05");
//    }
//    public void createRandomBarGraph(String Date1, String Date2){
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//
//        try {
//            Date date1 = simpleDateFormat.parse(Date1);
//            Date date2 = simpleDateFormat.parse(Date2);
//
//            Calendar mDate1 = Calendar.getInstance();
//            Calendar mDate2 = Calendar.getInstance();
//            mDate1.clear();
//            mDate2.clear();
//
//            mDate1.setTime(date1);
//            mDate2.setTime(date2);
//
//            dates = new ArrayList<>();
//            dates = getList(mDate1, mDate2);
//
//            ArrayList<BarEntry> yVals = new ArrayList<>();
//            random = new Random();
//            for (int i = 0; i < dates.size(); i++){
//                float value = random.nextFloat()*100f;
//                yVals.add(new BarEntry(i, (int)value));
//            }
//
//            BarDataSet set = new BarDataSet(yVals, "");
//            set.setColors(ColorTemplate.COLORFUL_COLORS);
//            set.setDrawValues(true);
//
//            BarData data = new BarData(set);
//        Legend legend = mChart.getLegend();
//        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//        mChart.setData(data);
//        mChart.invalidate();
//        mChart.animateY(500);
//        mChart.getLegend().setEnabled(false);
//        mChart.getDescription().setText(" ");
//        mChart.getAxisRight().setDrawLabels(false);
//
//        XAxis xAxis1= mChart.getXAxis();
//        xAxis1.setValueFormatter(new MyAxisValueFormatter(dates));
//
//
//        }catch (ParseException e){
//            e.printStackTrace();;
//        }
//
//
//    }
//    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
//        ArrayList<String> list = new ArrayList<>();
//        while(startDate.compareTo(endDate)<=0){
//            list.add(getDate(startDate));
//            startDate.add(Calendar.DAY_OF_MONTH, 1);
//        }
//        return list;
//    }
//    public String getDate(Calendar cld){
//        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH)+1)
//                + "/" + cld.get(Calendar.DAY_OF_MONTH);
//
//        try{
//            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
//            curDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
//        }catch (ParseException e)
//        {
//            e.printStackTrace();
//        }
//        return curDate;
//    }
//    public class MyAxisValueFormatter implements IAxisValueFormatter{
//        private ArrayList<String> mValues;
//        public MyAxisValueFormatter(ArrayList<String> values) {
//            this.mValues = values;
//        }
//
//        @Override
//        public String getFormattedValue(float value, AxisBase axis) {
//           // return mValues[(int)value];
//            return null;
//        }
//    }
}


