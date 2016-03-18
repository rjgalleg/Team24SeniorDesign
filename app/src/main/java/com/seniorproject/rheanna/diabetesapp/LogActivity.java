package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity implements View.OnClickListener {
    private Button log_activity;
    private Button back;
    private FitBitAuth fba;
    private static String ACTIVITY_URL = "https://api.fitbit.com/1/activities.json";
    private WebView webview;
    private Spinner spinner;
    private EditText edit_duration;
    private EditText edit_calories;
    private EditText edit_date;
    private EditText edit_distance;
    private EditText edit_startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        log_activity = (Button) findViewById(R.id.log_activity_button);
        back = (Button) findViewById(R.id.log_back_button);
        webview = (WebView) findViewById(R.id.log_activity_webview);
        spinner = (Spinner) findViewById(R.id.spinner1);
        edit_duration = (EditText) findViewById(R.id.edit_duration);
        edit_calories = (EditText) findViewById(R.id.edit_calories);
        edit_date = (EditText) findViewById(R.id.edit_date);
        edit_distance = (EditText) findViewById(R.id.edit_distance);
        edit_startTime = (EditText) findViewById(R.id.edit_startTime);

        log_activity.setOnClickListener(this);
        back.setOnClickListener(this);

        fba = new FitBitAuth(webview, ACTIVITY_URL, LogActivity.this);
        fba.Authorize(new GetUserJSONCallback() {
            @Override
            public void done(JSONObject jObject) {
                if (jObject == null) {
                } else {
                    setActivities(jObject);
                }

            }
        });
    }

    public void setActivities(JSONObject jObject){
        //String[] items = new String[400];
        List<String> items = new ArrayList<String>();
        int index = 1;
        try {
            items.add("Select Activity");
            JSONArray jarray = jObject.getJSONArray("categories");
            for(int i = 0; i<jarray.length(); i++) {
                JSONObject activities = jarray.getJSONObject(i);
                if (activities.has("activities")) {
                    JSONArray activity = activities.getJSONArray("activities");
                    for (int j = 0; j < activity.length(); j++) {
                        JSONObject jobj = activity.getJSONObject(j);
                        if (jobj.has("activityLevels")) {
                            JSONArray activity_level = jobj.getJSONArray("activityLevels");
                            for (int k = 0; k < activity_level.length(); k++) {
                                JSONObject level_object = activity_level.getJSONObject(k);
                                //items[index] = level_object.getString("name");
                                items.add(level_object.getString("name"));
                                index++;
                            }
                        }
                        // items[in
                        // dex] = jobj.getString("name");
                        items.add(jobj.getString("name"));
                        index++;
                    }


                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.log_activity_button:
                logActivity();
                /*edit_distance.setText("");
                edit_duration.setText("");
                edit_calories.setText("");
                edit_date.setText("");
                edit_startTime.setText(""); */
                break;
            case R.id.log_back_button:
                startActivity(new Intent(this, Activity.class));
                break;
        }
    }

    private void logActivity(){
        String activity = String.valueOf(spinner.getSelectedItem());
        String distance = edit_distance.getText().toString();
        String duration = edit_duration.getText().toString();
        String calories = edit_calories.getText().toString();
        String date = edit_date.getText().toString();
        String startTime = edit_startTime.getText().toString();

        if(distance.contains("0.0")) {
            fba.postLog(activity, startTime, duration, date, "0.00", calories, new GetUserJSONCallback() {
                @Override
                public void done(JSONObject jObject) {
                    setToast(jObject);
                }
            });
        }else{
            fba.postLog(activity, startTime, duration, date, distance, calories, new GetUserJSONCallback() {
                @Override
                public void done(JSONObject jObject) {
                    setToast(jObject);
                }
            });
        }

    }
    private void setToast(JSONObject object){
        try {

                Toast.makeText(LogActivity.this, "Activity Logged" ,Toast.LENGTH_LONG).show();
            /*} else {
                JSONObject error = error_array.getJSONObject(0);
                String message = error.getString("message");
                Toast.makeText(LogActivity.this, message ,Toast.LENGTH_LONG).show();
            }*/
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
