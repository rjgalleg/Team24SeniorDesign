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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HappinessActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout menu_layout;
    private LinearLayout quiz_layout;
    private LinearLayout chart_layout;
    private Button back;
    private Button sub_back;
    private Button sub_back2;
    private LineChart happy_chart;
    private RadioGroup quiz;
    private RadioButton very_happy;
    private RadioButton moderately_happy;
    private RadioButton slightly_happy;
    private RadioButton not_happy;
    private Button log;
    private Button happy_chart_button;
    private Button happy_log_quiz_button;
    String username = "rjgalleg";
    JSONArray jarray;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happiness);

        menu_layout = (LinearLayout) findViewById(R.id.menu_layout);
        quiz_layout = (LinearLayout) findViewById(R.id.quiz_layout);
        chart_layout = (LinearLayout) findViewById(R.id.chart_layout);
        back = (Button) findViewById(R.id.happy_back_button);
        sub_back = (Button) findViewById(R.id.happy_sub_back_button1);
        sub_back2 = (Button) findViewById(R.id.happy_sub_back_button2);
        log = (Button) findViewById(R.id.happy_log_button);
        happy_chart_button = (Button) findViewById(R.id.happy_chart_button);
        happy_log_quiz_button = (Button) findViewById(R.id.happy_log_quiz_button);
        happy_chart = (LineChart) findViewById(R.id.happy_chart);

        quiz = (RadioGroup) findViewById(R.id.quiz);
        very_happy = (RadioButton) findViewById(R.id.very_happy);
        moderately_happy = (RadioButton) findViewById(R.id.moderately_happy);
        slightly_happy = (RadioButton) findViewById(R.id.slightly_happy);
        not_happy = (RadioButton) findViewById(R.id.not_happy);

        quiz_layout.setVisibility(View.GONE);
        chart_layout.setVisibility(View.GONE);

        sub_back.setOnClickListener(this);
        back.setOnClickListener(this);
        sub_back2.setOnClickListener(this);
        log.setOnClickListener(this);
        happy_chart_button.setOnClickListener(this);
        happy_log_quiz_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.happy_log_button:
                quiz_layout.setVisibility(View.VISIBLE);
                chart_layout.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                break;
            case R.id.happy_chart_button:
                getData();
                quiz_layout.setVisibility(View.GONE);
                chart_layout.setVisibility(View.VISIBLE);
                menu_layout.setVisibility(View.GONE);
                break;
            case R.id.happy_back_button:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.happy_log_quiz_button:
                int selectedId = quiz.getCheckedRadioButtonId();
                Calendar c = Calendar.getInstance();
                int date = c.get(Calendar.DATE);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                String str_date = String.valueOf(date);
                String str_month = String.valueOf(month);
                String str_year = String.valueOf(year);
                String current_date = str_month + "/" + str_date + "/" + str_year;
                if(selectedId == very_happy.getId()) {
                    userData = new UserData(username, current_date, "4");
                } else if(selectedId == moderately_happy.getId()) {
                    userData = new UserData(username, current_date, "3");
                } else if(selectedId == slightly_happy.getId()) {
                    userData = new UserData(username,current_date, "2");
                } else if(selectedId == slightly_happy.getId()) {
                    userData = new UserData(username, current_date, "1");
                }
                logInfo(userData);

                quiz_layout.setVisibility(View.GONE);
                chart_layout.setVisibility(View.GONE);
                menu_layout.setVisibility(View.VISIBLE);

                break;
            case R.id.happy_sub_back_button1:
                quiz_layout.setVisibility(View.GONE);
                chart_layout.setVisibility(View.GONE);
                menu_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.happy_sub_back_button2:
                quiz_layout.setVisibility(View.GONE);
                chart_layout.setVisibility(View.GONE);
                menu_layout.setVisibility(View.VISIBLE);
                break;

        }

    }

    private void logInfo(UserData userdata){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserHappyDataInBackground(userdata, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(HappinessActivity.this, HappinessActivity.class));
            }
        });
    }

    private void getData(){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserHappyDataInBackground(username, new GetUserDataCallback() {
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
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(HappinessActivity.this);
        dialogueBuilder.setMessage("Error");
        dialogueBuilder.setPositiveButton("OK", null);
        dialogueBuilder.show();
    }


    public void displayGraphic(JSONArray jarray){
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        happy_chart.setViewPortOffsets(0, 20, 0, 0);

        // no description text
        happy_chart.setDescription("");

        // enable touch gestures
        happy_chart.setTouchEnabled(true);

        // enable scaling and dragging
        happy_chart.setDragEnabled(true);
        happy_chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        happy_chart.setPinchZoom(false);

        happy_chart.setDrawGridBackground(true);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis x =happy_chart.getXAxis();
        x.setEnabled(false);

        YAxis y = happy_chart.getAxisLeft();
        y.setLabelCount(4, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(true);
        y.setAxisLineColor(Color.BLACK);

        happy_chart.getAxisRight().setEnabled(false);


        try {
            int j = 6; int i;
            for (i = 0; i < jarray.length(); i++) {
                JSONObject jobject = jarray.getJSONObject(i);
                String date = jobject.getString("date");
                String value = jobject.getString("value");
                int val = Integer.parseInt(value);
                if(!xVals.contains(date)){
                    if(value.equals("4")){
                        yVals1.add(new Entry(val, i, "Very Happy"));
                    }else if(value.equals("3")){
                        yVals1.add(new Entry(val, i, "Moderately Happy"));
                    }else if(value.equals("2")){
                        yVals1.add(new Entry(val, i, "Slightly Happy"));
                    }else if(value.equals("1")){
                        yVals1.add(new Entry(val, i, "Not Happy"));
                    }
                    xVals.add(date);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        LineDataSet set1 = new LineDataSet(yVals1, "Emotional Trend");
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
        happy_chart.setData(data);
        happy_chart.getLegend().setEnabled(false);
        happy_chart.animateXY(2000, 2000);

        // dont forget to refresh the drawing
        happy_chart.invalidate();



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
