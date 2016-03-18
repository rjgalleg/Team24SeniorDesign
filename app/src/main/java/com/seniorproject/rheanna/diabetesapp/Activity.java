package com.seniorproject.rheanna.diabetesapp;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity extends AppCompatActivity implements View.OnClickListener {
    private Button back_button, weekly_activity, daily_activity, monthly_activity, log_activity;
    private LinearLayout weekly_menu_layout, weekly_calories_layout, chart_activity_layout, get_activity_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        weekly_activity = (Button) findViewById(R.id.weekly_activity_button);
        daily_activity = (Button) findViewById(R.id.daily_activity_button);
        monthly_activity = (Button) findViewById(R.id.monthly_activity_button);
        back_button = (Button) findViewById(R.id.back_activity_button);
        log_activity = (Button) findViewById(R.id.log_activity_button);

        back_button.setOnClickListener(this);
        weekly_activity.setOnClickListener(this);
        monthly_activity.setOnClickListener(this);
        daily_activity.setOnClickListener(this);
        log_activity.setOnClickListener(this);

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


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.daily_activity_button:
                startActivity(new Intent(this, DailyActivity.class));
                break;
            case R.id.weekly_activity_button:
                startActivity(new Intent(this, WeeklyActivity.class));
                break;
            case R.id.monthly_activity_button:
                startActivity(new Intent(this, MonthlyActivity.class));
                break;
            case R.id.log_activity_button:
                startActivity(new Intent(this, LogActivity.class));
                break;
            case R.id.back_activity_button:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
