package com.example.ivlm.goodbudget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by _IVLM on 3/15/2016.
 */
public class Reports_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_fragment, container, false);
        BarChart barChart = (BarChart) view.findViewById(R.id.chart);
        ShowVerticalBar(barChart);
        return view;
    }

    public void ShowVerticalBar(BarChart barChart){

        double test = 12.55;
        DecimalFormat decimalFormat = new DecimalFormat("#");
        Float test2 = (float) test;
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4,0));
        entries.add(new BarEntry(8,1));
        entries.add(new BarEntry(6,2));
        entries.add(new BarEntry(12, 3));


        BarDataSet barDataSet = new BarDataSet(entries,"first");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> label = new ArrayList<>();
        label.add("January");
        label.add("February");
        label.add("March");
        label.add("April");

        BarData data = new BarData(label, barDataSet);
        barChart.setData(data);
        barChart.setDescription("Description");
        barChart.animateY(5000);
    }
    @Override
    public void onResume() {
        super.onResume();

        Toast.makeText(getActivity(), "Reports onResume", Toast.LENGTH_SHORT).show();
    }
}
