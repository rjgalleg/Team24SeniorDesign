package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DailyActivity extends AppCompatActivity implements View.OnClickListener {
    private static String DAILY_RESOURCE_SUMMARY = "https://api.fitbit.com/1/user/-/activities/date/"; //[date].json"
    private PieChart summary_chart;
    private TextView goal_calories;
    private TextView achieved_calories;
    private TextView goal_distance;
    private TextView achieved_distance;
    private TextView goal_minutes;
    private TextView achieved_minutes;
    private TextView goal_steps;
    private TextView achieved_steps;
    private Button get_activity_button;
    private EditText date;
    private Button sub_back_button;

    private WebView activity_webview;

    private LinearLayout get_activity_layout;
    private LinearLayout chart_activity_layout;
    private LinearLayout webview_activity_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);


        sub_back_button = (Button) findViewById(R.id.sub_back_button);
        activity_webview = (WebView) findViewById(R.id.activity_webview);
        summary_chart = (PieChart) findViewById(R.id.activity_summary);
        goal_calories = (TextView) findViewById(R.id.goal_calories);
        achieved_calories = (TextView) findViewById(R.id.achieved_calories);
        goal_distance = (TextView) findViewById(R.id.goal_distance);
        achieved_distance = (TextView) findViewById(R.id.achieved_distance);
        goal_minutes = (TextView) findViewById(R.id.goal_minutes);
        achieved_minutes= (TextView) findViewById(R.id.achieved_minutes);
        goal_steps = (TextView) findViewById (R.id.goal_steps);
        achieved_steps = (TextView) findViewById(R.id.achieved_steps);

        get_activity_button = (Button) findViewById(R.id.get_activity_button);

        date = (EditText) findViewById(R.id.activity_date);

        get_activity_layout = (LinearLayout) findViewById(R.id.get_activity_layout);
        chart_activity_layout = (LinearLayout) findViewById(R.id.chart_activity_layout);
        webview_activity_layout = (LinearLayout) findViewById(R.id.webview_activity_layout);

        get_activity_button.setOnClickListener(this);

        get_activity_layout.setVisibility(View.VISIBLE);
        chart_activity_layout.setVisibility(View.GONE);
        webview_activity_layout.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.get_activity_button:
                String Date = date.getText().toString();
                getData(Date, DAILY_RESOURCE_SUMMARY);

                get_activity_layout.setVisibility(v.GONE);
                chart_activity_layout.setVisibility(v.VISIBLE);
                webview_activity_layout.setVisibility(v.VISIBLE);
                break;
            case R.id.sub_back_button:
                startActivity(new Intent(this, Activity.class));
        }
    }

    private void getData(String date, String url_type){
        final String URL = url_type + date + ".json";
        final String d = date;
        //String URL = DAILY_RESOURCE_GOAL;
        FitBitAuth fba = new FitBitAuth(activity_webview, URL);
        fba.Authorize(new GetUserJSONCallback() {
            @Override
            public void done(JSONObject jObject) {
                Log.i("Authorize", "Activity Summary: " + jObject);
                if (jObject == null) {
                } else {
                    setChart(jObject, d);
                }

            }
        });


    }

    private void setChart(JSONObject jObject, String date) {
        try {
            JSONObject goals = jObject.getJSONObject("goals");
            JSONObject summary = jObject.getJSONObject("summary");
            JSONArray distances = summary.getJSONArray("distances");
            JSONObject veryActive = distances.getJSONObject(3);
            JSONObject moderatelyActive = distances.getJSONObject(4);
            JSONObject lightlyActive = distances.getJSONObject(5);
            JSONObject sedentaryActive = distances.getJSONObject(6);
            JSONObject totalDistance = distances.getJSONObject(0);
            String va = veryActive.getString("distance");
            String ma = moderatelyActive.getString("distance");
            String la = lightlyActive.getString("distance");
            String sa = sedentaryActive.getString("distance");
            float totalMinutes = Float.parseFloat(va)+ Float.parseFloat(ma)+ Float.parseFloat(la) + Float.parseFloat(sa);
            goal_minutes.setText(goals.getString("activeMinutes"));
            achieved_minutes.setText(Float.toString(totalMinutes));
            goal_calories.setText(goals.getString("caloriesOut"));
            achieved_calories.setText(summary.getString("caloriesOut"));
            goal_distance.setText(goals.getString("distance"));
            achieved_distance.setText(totalDistance.getString("distance"));
            goal_steps.setText(goals.getString("steps"));
            achieved_steps.setText(summary.getString("steps"));


            //pie chart settings
            summary_chart.setUsePercentValues(true);
            summary_chart.setDescription("");
            //summary_chart.setExtraOffsets(5, 10, 5, 5);
            summary_chart.setDragDecelerationFrictionCoef(0.95f);
            summary_chart.setDrawHoleEnabled(true);
            summary_chart.setHoleColor(Color.WHITE);
            summary_chart.setTransparentCircleColor(Color.WHITE);
            //summary_chart.setTransparentCircleAlpha(110);
            // summary_chart.setHoleRadius(20f);
            //summary_chart.setTransparentCircleRadius(61f);
            summary_chart.setRotationAngle(0);
            // enable rotation of the chart by touch
            summary_chart.setRotationEnabled(true);
            summary_chart.setHighlightPerTapEnabled(true);

            //get info from FitBit Account
            // set data
            setData(jObject);
            summary_chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            //Legend
            Legend l = summary_chart.getLegend();
            l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void setData(JSONObject jObject) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        try {
            JSONObject summary = jObject.getJSONObject("summary");
            int fairlyActiveMinutes = summary.getInt("fairlyActiveMinutes");
            int lightlyActiveMinutes = summary.getInt("lightlyActiveMinutes");
            int sedentaryMinutes = summary.getInt("sedentaryMinutes");
            int veryActiveMinutes = summary.getInt("veryActiveMinutes");
            yVals1.add(new Entry(veryActiveMinutes, 0));
            yVals1.add(new Entry(fairlyActiveMinutes, 1));
            yVals1.add(new Entry(lightlyActiveMinutes, 2));
            yVals1.add(new Entry(sedentaryMinutes, 3));

            String[] mParties = {"Very Active", "Moderately Active", "Lightly Active", "Sedimentary Active"};
            ArrayList<String> xVals = new ArrayList<String>();

            for (int i = 0; i < 4; i++) {
                xVals.add(mParties[i]);
            }

            PieDataSet dataSet = new PieDataSet(yVals1, "Active Minutes");
            dataSet.setSliceSpace(3f);
            //dataSet.setSelectionShift(5f);

            // add a lot of colors
            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);

            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            summary_chart.setData(data);

            // undo all highlights
            summary_chart.highlightValues(null);

            summary_chart.invalidate();
        }catch(Exception e){
            e.printStackTrace();
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

        switch (id) {
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
