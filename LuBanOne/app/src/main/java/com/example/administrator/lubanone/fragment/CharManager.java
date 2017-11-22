package com.example.administrator.lubanone.fragment;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quyang on 2017/6/28.
 */

public class CharManager {


    private Context mContext;

    public CharManager(Context context) {
        this.mContext = context;
    }


    private List<String> getLabels() {
        List<String> chartLabels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chartLabels.add("X" + i);
        }
        return chartLabels;
    }


    public BarData getBarData() {
        BarDataSet dataSetA = new BarDataSet(getChartData(), "");
        //設定顏色
        dataSetA.setColors(getChartColors());
        //設定顯示字串
        dataSetA.setStackLabels(getStackLabels());

        List<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetA); // add the datasets

        List<String> labels = getLabels();
        BarData barData = new BarData(labels, dataSets);
//        barData.setDrawValues(false);
        return barData;
    }

    private String[] getStackLabels() {
        return new String[]{"a", "b"};
    }

    private int[] getChartColors() {
        int[] colors = new int[]{Color.RED, Color.GREEN};
        return colors;
    }

    private int getResourceColor(int resID) {
        return mContext.getResources().getColor(resID);
    }

    private List<BarEntry> getChartData() {
        final int DATA_COUNT = 5;//柱子个数 这个数字不能大于labels的个数

        List<BarEntry> chartData = new ArrayList<>();
        //每一個月都有四筆資料
        for (int i = 0; i < DATA_COUNT; i++) {
            float shouyi = i * 3;
            float zhichu = i * 3;
            //控制一个柱子上的布局
            chartData.add(new BarEntry(new float[]{shouyi, zhichu}, i));
        }
        return chartData;
    }

    public void configChartAxis(BarChart chart_bar) {
        XAxis xAxis = chart_bar.getXAxis();
        xAxis.setDrawGridLines(false);

        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftYAxis = chart_bar.getAxisLeft();
        leftYAxis.setDrawGridLines(false);

        YAxis RightYAxis = chart_bar.getAxisRight();
        RightYAxis.setEnabled(false);   //不顯示右側
    }

    public void configChartAxis(LineChart chart_bar) {
        XAxis xAxis = chart_bar.getXAxis();
        xAxis.setDrawGridLines(false);

        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftYAxis = chart_bar.getAxisLeft();
        leftYAxis.setDrawGridLines(false);

        YAxis RightYAxis = chart_bar.getAxisRight();
        RightYAxis.setEnabled(false);   //不顯示右側
    }
}
