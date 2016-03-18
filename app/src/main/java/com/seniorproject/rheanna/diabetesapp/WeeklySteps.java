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
import com.github.mikephil.charting.charts.HorizontalBarChart;
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

public class WeeklySteps extends AppCompatActivity implements View.OnClickListener {
    private FitBitAuth fba;
    private static String WEEKLY_STEPS_URL = "https://api.fitbit.com/1/user/-/activities/steps/date/today/1w.json";
    private HorizontalBarChart chart;
    private WebView webview;
    private Button back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_steps);

        back_button = (Button) findViewById(R.id.weekly_steps_back);
        webview = (WebView) findViewById(R.id.weekly_steps_webview);
        chart = (HorizontalBarChart) findViewById(R.id.steps_chart);

        chart.setVisibility(View.GONE);
        setHorizontalBarChart();
        webview.setVisibility(View.GONE);
        chart.setVisibility(View.VISIBLE);

        back_button.setOnClickListener(this);
    }

    private void setHorizontalBarChart(){
        // mChart.setHighlightEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        chart.setDrawGridBackground(false);

        // mChart.setDrawYLabels(false);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = chart.getAxisLeft();
        //yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = chart.getAxisRight();
        //yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        fba = new FitBitAuth(webview, WEEKLY_STEPS_URL, WeeklySteps.this);
        fba.Authorize(new GetUserJSONCallback() {
            @Override
            public void done(JSONObject jObject) {
                Log.i("Authorize", "WEEKLY calories: " + jObject);
                if (jObject == null) {
                } else {
                    setData(jObject);
                }

            }
        });
        chart.animateY(2500);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        // mChart.setDrawLegend(false);

    }

    private void setData(JSONObject jObject){
        try {
            JSONArray jArray = jObject.getJSONArray("activities-steps");

            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jobj = jArray.getJSONObject(i);
                xVals.add(jobj.getString("dateTime"));
                yVals1.add(new BarEntry(jobj.getInt("value"), i));
            }

            BarDataSet set1 = new BarDataSet(yVals1, "Steps");

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);
            chart.setData(data);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.weekly_steps_back:
                startActivity(new Intent(this, WeeklyActivity.class));
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
