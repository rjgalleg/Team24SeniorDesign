package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MonthlyCalories extends AppCompatActivity implements View.OnClickListener {
    private FitBitAuth fba;
    private static String WEEKLY_CALORIES_URL = "https://api.fitbit.com/1/user/-/activities/calories/date/today/1m.json";
    private BarChart calories_chart;
    private WebView webview;
    private Button back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_calories);

        back_button = (Button) findViewById(R.id.monthly_calories_back);
        webview = (WebView) findViewById(R.id.monthly_calories_webview);
        calories_chart = (BarChart) findViewById(R.id.monthly_calories_chart);

        calories_chart.setVisibility(View.GONE);
        setBarChart();
        webview.setVisibility(View.GONE);
        calories_chart.setVisibility(View.VISIBLE);

        back_button.setOnClickListener(this);
    }

    private void setBarChart(){
        calories_chart.setDrawBarShadow(false);
        calories_chart.setDrawValueAboveBar(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        calories_chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        calories_chart.setPinchZoom(false);

        calories_chart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = calories_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        //YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = calories_chart.getAxisLeft();
        //leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = calories_chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8, false);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = calories_chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        fba = new FitBitAuth(webview, WEEKLY_CALORIES_URL, MonthlyCalories.this);
        fba.Authorize(new GetUserJSONCallback() {
            @Override
            public void done(JSONObject jObject) {
                Log.i("Authorize", "WEEKLY calories: " + jObject);
                if (jObject == null) {
                } else {
                    setBarChartData(jObject);
                }

            }
        });

    }

    private void setBarChartData(JSONObject jObject){

        try {
            JSONArray jArray = jObject.getJSONArray("activities-calories");

            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jobj = jArray.getJSONObject(i);
                xVals.add(jobj.getString("dateTime"));
                yVals1.add(new BarEntry(jobj.getInt("value"), i));
            }

            BarDataSet set1 = new BarDataSet(yVals1, "Calories Burned");
            set1.setBarSpacePercent(35f);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);
            // data.setValueTypeface(mTf);

            calories_chart.setData(data);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.monthly_calories_back:
                startActivity(new Intent(this, MonthlyActivity.class));
                break;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_menu:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.action_activity:
                startActivity(new Intent(this, Activity.class));
                break;
            case R.id.action_food:
                startActivity(new Intent(this, FoodLog.class));
                break;
            case R.id.action_forum:
                break;
            case R.id.action_happy:
                startActivity(new Intent(this, HappinessActivity.class));
                break;
            case R.id.action_glucose:
                startActivity(new Intent(this, GlucoseMenu.class));
                break;
            case R.id.action_logout:
                startActivity(new Intent(this, Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
