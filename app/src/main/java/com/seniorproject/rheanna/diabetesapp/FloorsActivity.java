package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FloorsActivity extends AppCompatActivity implements View.OnClickListener {
    private FitBitAuth fba;
    private static String WEEKLY_FLOORS_URL = "https://api.fitbit.com/1/user/-/activities/floors/date/today/1w.json";
    private LineChart floors_chart;
    private WebView webview;
    private Button back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floors);

        floors_chart = (LineChart) findViewById(R.id.floors_chart);
        back_button = (Button) findViewById(R.id.weekly_floors_back);
        webview = (WebView) findViewById(R.id.weekly_floors_webview);

        floors_chart.setVisibility(View.GONE);
        setLineChart();
        floors_chart.setVisibility(View.VISIBLE);
        webview.setVisibility(View.GONE);

        back_button.setOnClickListener(this);

    }


    public void setLineChart(){
        floors_chart.setViewPortOffsets(0, 20, 0, 0);
        // enable touch gestures
        floors_chart.setTouchEnabled(true);
        // enable scaling and dragging
        floors_chart.setDragEnabled(true);
        floors_chart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        floors_chart.setPinchZoom(false);
        floors_chart.setDrawGridBackground(false);

        // Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis x = floors_chart.getXAxis();
        x.setEnabled(true);
        floors_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis y = floors_chart.getAxisLeft();
        //y.setTypeface(tf);
        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.BLACK);

        floors_chart.getAxisRight().setEnabled(false);

        fba = new FitBitAuth(webview, WEEKLY_FLOORS_URL, FloorsActivity.this);
        fba.Authorize(new GetUserJSONCallback() {
            @Override
            public void done(JSONObject jObject) {
                if (jObject == null) {
                } else {
                    setLineChartData(jObject);
                }

            }
        });

        floors_chart.getLegend().setEnabled(false);
        floors_chart.animateXY(2000, 2000);
        // dont forget to refresh the drawing
        floors_chart.invalidate();
    }

    private void setLineChartData(JSONObject jobject) {
        try {
            JSONArray jarray = jobject.getJSONArray("activities-floors");
            ArrayList<Entry> vals1 = new ArrayList<Entry>();
            ArrayList<String> xVals = new ArrayList<String>();
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jobj = jarray.getJSONObject(i);
                xVals.add(jobj.getString("dateTime"));
                vals1.add(new Entry(jobj.getInt("value"), i));
            }

            // create a dataset and give it a type
            LineDataSet set1 = new LineDataSet(vals1, "Floors");
            set1.setDrawCubic(true);
            set1.setCubicIntensity(0.2f);
            //set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.BLACK);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.BLACK);
            set1.setFillColor(Color.BLACK);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new FillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data = new LineData(xVals, set1);
            // data.setValueTypeface(tf);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            floors_chart.setData(data);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.weekly_floors_back:
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
