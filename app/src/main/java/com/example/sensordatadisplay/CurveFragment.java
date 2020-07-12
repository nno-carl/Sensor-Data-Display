package com.example.sensordatadisplay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class CurveFragment extends Fragment {
    public static Curve dynamicLineChartManager;
    private List<Float> list = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<Integer> color = new ArrayList<>();
    private View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.curve_layout, container, false);
        //create curves
        LineChart mChart = (LineChart) view.findViewById(R.id.dynamic_chart);
        names.add("X");
        color.add(Color.BLUE);
        names.add("Y");
        color.add(Color.RED);
        names.add("Z");
        color.add(Color.BLACK);
        dynamicLineChartManager = new Curve(mChart, names, color);
        dynamicLineChartManager.setYAxis(180, -180, 1);
        return view;
    }
}
