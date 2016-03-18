package com.seniorproject.rheanna.diabetesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

public class ViewGlucose extends AppCompatActivity implements View.OnClickListener {
    private Button get_info, glucose_back2;
    private static String username = "rjgalleg";
    private LinearLayout get_info_layout, view_glucose_layout;
    private EditText date;
    private TextView date_view, glucose_view, tag_view, medication_view, hba1c_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_glucose);

        get_info = (Button) findViewById(R.id.view_glucose_info_button);
        get_info_layout = (LinearLayout) findViewById(R.id.get_glucose_layout);
        date = (EditText) findViewById(R.id.glucose_date);
        view_glucose_layout = (LinearLayout) findViewById(R.id.view_glucose_layout);
        date_view = (TextView) findViewById(R.id.date_view);
        glucose_view = (TextView) findViewById(R.id.glucose_view);
        tag_view = (TextView) findViewById(R.id.tag_view);
        medication_view = (TextView) findViewById(R.id.medication_view);
        hba1c_view = (TextView) findViewById(R.id.hba1c_view);
        glucose_back2 = (Button) findViewById(R.id.glucose_back2);

        get_info.setOnClickListener(this);
        view_glucose_layout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.view_glucose_info_button:
                get_info_layout.setVisibility(v.GONE);
                view_glucose_layout.setVisibility(v.VISIBLE);
                String str_date = date.getText().toString();
                getData(str_date);
                break;
            case R.id.glucose_back2:
                startActivity(new Intent(this, GlucoseMenu.class));
                break;
            case R.id.glucose_back:
                startActivity(new Intent(this, GlucoseMenu.class));
                break;
        }
    }

    public void getData(String date){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserGlucoseInBackground(date, new GetUserDataCallback() {
            @Override
            public void done(JSONArray jarray) {
                if (jarray == null) {
                    showErrorMessage();
                } else {
                    displayData(jarray);
                }

            }
        });

    }
    private void showErrorMessage(){
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(ViewGlucose.this);
        dialogueBuilder.setMessage("Error");
        dialogueBuilder.setPositiveButton("OK", null);
        dialogueBuilder.show();
    }

    public void displayData(JSONArray jarray){
        try{
            JSONObject jobject = jarray.getJSONObject(0);
            String date = jobject.getString("date");
            String number = jobject.getString("number") +"mg/dL";
            String tag = jobject.getString("tag");
            String medication = "Medication: "+jobject.getString("medication");
            String hba1c = "HbA1c: "+jobject.getString("hba1c") + "%";

            date_view.setText(date);
            glucose_view.setText(number);
            tag_view.setText(tag);
            medication_view.setText(medication);
            hba1c_view.setText(hba1c);
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
