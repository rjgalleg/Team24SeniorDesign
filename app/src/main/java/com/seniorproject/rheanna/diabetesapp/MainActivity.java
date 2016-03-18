package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button sleep_button, stress_button, calorie_button, main_glucose_button, emotional_log_button, forum_button;
    EditText Username;
    HorizontalBarChart chart;
    UserLocalStore userLocalStore;
    FitBitAuth fba;
    WebView webview;
    String url ="https://api.fitbit.com/1/user/-/activities/steps/date/today/1w.json";
    JSONObject steps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Username = (EditText) findViewById(R.id.Username);
        sleep_button = (Button) findViewById(R.id.sleep_button);
        stress_button = (Button) findViewById(R.id.stress_button);
        calorie_button = (Button) findViewById(R.id.heart_button);
        main_glucose_button = (Button) findViewById(R.id.main_glucose_button);
        forum_button = (Button) findViewById(R.id.forum_button);
        //friends_button = (Button) findViewById(R.id.friends_button);
        emotional_log_button = (Button) findViewById(R.id.emotion_log_button);
        webview = (WebView) findViewById(R.id.main_webview);
        chart = (HorizontalBarChart) findViewById(R.id.graph);
        forum_button.setOnClickListener(this);
        sleep_button.setOnClickListener(this);
        stress_button.setOnClickListener(this);
        calorie_button.setOnClickListener(this);
        emotional_log_button.setOnClickListener(this);
        main_glucose_button.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        webview.requestFocus(View.FOCUS_DOWN);
        webview.setVisibility(View.VISIBLE);
      //  logout_button.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authenticate() == true) {

                fba = new FitBitAuth(webview, url);
                fba.Authorize(new GetUserJSONCallback() {
                    @Override
                    public void done(JSONObject jObject) {
                        if (jObject == null) {
                        } else {
                            getSteps(jObject);
                        }

                    }
                });


        } else{
            startActivity(new Intent(MainActivity.this, Login.class));
        }

    }

    private void getSteps(JSONObject jObject){
        String date; String value; String[] dates = {"", "", "", "", "", "", ""}; String[] values = {"", "", "", "", "", "", ""};
        try {
            JSONArray steps = jObject.getJSONArray("activities-steps");
            for (int i = 0; i < steps.length(); i++) {
                JSONObject obj = steps.getJSONObject(i);
                date = obj.getString("dateTime");
                value = obj.getString("value");
                dates[i] = date;
                values[i] = value;
            }

            chart.setDrawBarShadow(false);
            chart.setDescription("");
            Integer I;
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            ArrayList<String> xVals = new ArrayList<String>();
            for(int j = 0; j<steps.length(); j++){
                xVals.add(dates[j]);
                yVals1.add(new BarEntry(Integer.parseInt(values[j]), j));
            }

            BarDataSet set1 = new BarDataSet(yVals1, "Steps");
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(xVals, dataSets);
            chart.setData(data);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.stress_button:
                startActivity(new Intent(this, Activity.class));
                break;
            case R.id.heart_button:
                startActivity(new Intent(this, FoodLog.class));
                break;
            case R.id.sleep_button:
                startActivity(new Intent(this, SleepLog.class));
                break;
            case R.id.emotion_log_button:
                startActivity(new Intent(this, HappinessActivity.class));
                break;
            case R.id.main_glucose_button:
                startActivity(new Intent(this, GlucoseMenu.class));
                break;
            case R.id.forum_button:
                startActivity(new Intent(this, Menu_News_Feed.class));
                break;
        }

    }
}
