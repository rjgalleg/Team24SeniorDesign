package com.seniorproject.rheanna.diabetesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SleepLog extends AppCompatActivity implements View.OnClickListener {

    CombinedChart chart;
    Button sleep_pattern, log, log_sleep, back, back2;
    EditText log_hour, log_date;
    UserData userdata;
    String username = "rjgalleg";
    JSONArray jarray;
    TextView options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_log);

        chart = (CombinedChart) findViewById(R.id.sleep_chart);
        back = (Button) findViewById(R.id.back_button);
        log_sleep = (Button) findViewById(R.id.log_sleep_button);
        sleep_pattern = (Button) findViewById(R.id.sleep_pattern_button);
        log_hour = (EditText) findViewById(R.id.sleep_hours);
        log_date = (EditText) findViewById(R.id.sleep_date);
        log = (Button) findViewById(R.id.log_button);
        back2 = (Button) findViewById(R.id.back_button2);
        options= (TextView) findViewById(R.id.options);

        log.setVisibility(View.GONE);
        log_hour.setVisibility(View.GONE);
        log_date.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        chart.setVisibility(View.GONE);

        back2.setOnClickListener(this);
        log.setOnClickListener(this);
        back.setOnClickListener(this);
        log_sleep.setOnClickListener(this);
        sleep_pattern.setOnClickListener(this);

        chart.setDescription("");
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.log_sleep_button:
                options.setVisibility(v.GONE);
                sleep_pattern.setVisibility(v.GONE);
                log_sleep.setVisibility(v.GONE);
                log_hour.setVisibility(v.VISIBLE);
                log_date.setVisibility(v.VISIBLE);
                log.setVisibility(v.VISIBLE);
                back.setVisibility(v.VISIBLE);
                back2.setVisibility(v.GONE);

                break;

            case R.id.sleep_pattern_button:
                options.setVisibility(v.GONE);
                sleep_pattern.setVisibility(v.GONE);
                log_sleep.setVisibility(v.GONE);
                log.setVisibility(v.GONE);
                chart.setVisibility(v.VISIBLE);
                back2.setVisibility(v.GONE);
                back.setVisibility(v.VISIBLE);

                getData();
                break;

            case R.id.back_button:
                options.setVisibility(v.VISIBLE);
                log_sleep.setVisibility(v.VISIBLE);
                sleep_pattern.setVisibility(v.VISIBLE);
                back2.setVisibility(v.VISIBLE);
                back.setVisibility(v.GONE);
                log.setVisibility(v.GONE);
                log_hour.setVisibility(v.GONE);
                log_date.setVisibility(v.GONE);
                chart.setVisibility(v.GONE);
                break;

            case R.id.log_button:
                String date = log_date.getText().toString();
                String hour = log_hour.getText().toString();

                userdata = new UserData(username, date, hour);
                logInfo(userdata);
                break;
            case R.id.back_button2:
                startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void logInfo(UserData userdata){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserSleepDataInBackground(userdata, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(SleepLog.this, SleepLog.class));
            }
        });
    }

    private void getData(){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserSleepDataInBackground(username, new GetUserDataCallback() {
            @Override
            public void done(JSONArray jarray) {
                if (jarray == null) {
                    showErrorMessage();
                } else {
                    displayGraphic(jarray);
                }

            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(SleepLog.this);
        dialogueBuilder.setMessage("Error");
        dialogueBuilder.setPositiveButton("OK", null);
        dialogueBuilder.show();
    }

    public void displayGraphic(JSONArray jarray){
        List<String> dates = new ArrayList<String>();
        try {
            int j = 6; int i;
            if(jarray.length() > 7){
                for (i = jarray.length() - 1; i > jarray.length() - 8; i--) {
                    JSONObject jobject = jarray.getJSONObject(i - j);
                    String date = jobject.getString("sleep_date");
                    dates.add(date);
                    j = j - 2;
                }
            }else {
                for (i = 0; i < jarray.length(); i++) {
                    JSONObject jobject = jarray.getJSONObject(i);
                    String date = jobject.getString("sleep_date");
                    dates.add(date);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        CombinedData data = new CombinedData(dates);
        data.setData(generateLineData(jarray));
        data.setData(generateBarData(jarray));

        chart.setData(data);
        chart.invalidate();


    }

    private LineData generateLineData(JSONArray jarray) {
        int total_hours = 0; int avg = 0;
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        try {
            int j = 6; int i;
            if(jarray.length() > 7) {
                for (i = jarray.length() - 1; i > jarray.length() - 8; i--) {
                    JSONObject jobject = jarray.getJSONObject(i - j);
                    String hours = jobject.getString("hours");
                    Integer hour = Integer.parseInt(hours);
                    total_hours = total_hours + hour;
                    avg = total_hours / (i - j + 1);
                    entries.add(new Entry(avg, i - j-1));
                    j = j - 2;
                }
            } else {
                for (i = 0; i < jarray.length(); i++) {
                    JSONObject jobject = jarray.getJSONObject(i);
                    String date = jobject.getString("sleep_date");
                    String hours = jobject.getString("hours");
                    Integer hour = Integer.parseInt(hours);
                    total_hours = total_hours + hour;
                    avg = total_hours / (i + 1);
                    entries.add(new Entry(avg, i));
                }
            }

            LineDataSet set = new LineDataSet(entries, "Average Sleep Hours");
            set.setColor(Color.rgb(240, 238, 70));
            set.setLineWidth(2.5f);
            set.setCircleColor(Color.rgb(240, 238, 70));
            set.setCircleRadius(5f);
            set.setFillColor(Color.rgb(240, 238, 70));
            set.setDrawCubic(true);
            set.setDrawValues(true);
            set.setValueTextSize(10f);
            set.setValueTextColor(Color.rgb(240, 238, 70));
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            d.addDataSet(set);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    private BarData generateBarData(JSONArray jarray) {

        BarData d = new BarData();
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        try {
            int j = 6; int i;
            if(jarray.length() > 7) {
                for (i = jarray.length() - 1; i > jarray.length() - 8; i--) {
                    JSONObject jobject = jarray.getJSONObject(i - j);
                    String hours = jobject.getString("hours");
                    Integer hour = Integer.parseInt(hours);
                    entries.add(new BarEntry(hour, i - j - 1));
                    j= j - 2;
                }
            }else{
                for (i = 0; i < jarray.length(); i++) {
                    JSONObject jobject = jarray.getJSONObject(i);
                    String hours = jobject.getString("hours");
                    Integer hour = Integer.parseInt(hours);
                    entries.add(new BarEntry(hour, i));
                }
            }

            BarDataSet set = new BarDataSet(entries, "Daily Sleep Schedule");
            set.setColor(Color.rgb(60, 220, 78));
            set.setValueTextColor(Color.rgb(60, 220, 78));
            set.setValueTextSize(10f);
            d.addDataSet(set);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return d;
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
