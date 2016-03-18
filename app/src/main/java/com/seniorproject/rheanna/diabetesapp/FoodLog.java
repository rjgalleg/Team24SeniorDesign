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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class FoodLog extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout get_log_layout;
    private LinearLayout view_log_layout;
    private LinearLayout food_log_webview_layout;
    private Button get_food;
    private Button food_log_back2;
    private EditText food_log_date;
    private TextView log_calories;
    private TextView log_carbs;
    private TextView log_fat;
    private TextView log_fiber;
    private TextView log_protein;
    private TextView log_sodium;
    private TextView log_water;
    private TextView log_calories_goal;
    private WebView food_log_webview;
    private FitBitAuth fba;
    private static String FOOD_URL = "https://api.fitbit.com/1/user/-/foods/log/date/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_log);

        get_log_layout = (LinearLayout) findViewById(R.id.get_log_layout);
        view_log_layout = (LinearLayout) findViewById(R.id.view_log_layout);
        food_log_webview_layout = (LinearLayout) findViewById(R.id.food_log_webview_layout);
        get_food = (Button) findViewById(R.id.get_food);
        food_log_back2 = (Button) findViewById(R.id.food_log_back2);
        food_log_date = (EditText) findViewById(R.id.food_log_date);
        log_calories = (TextView) findViewById(R.id.log_calories);
        log_carbs = (TextView) findViewById(R.id.log_carbs);
        log_fat = (TextView) findViewById(R.id.log_fat);
        log_fiber = (TextView) findViewById(R.id.log_fiber);
        log_protein = (TextView) findViewById(R.id.log_protein);
        log_sodium = (TextView) findViewById(R.id.log_sodium);
        log_water = (TextView) findViewById(R.id.log_water);
        log_calories_goal = (TextView) findViewById(R.id.log_calories_goal);
        food_log_webview = (WebView) findViewById(R.id.food_log_webview);
        get_food.setOnClickListener(this);
        food_log_back2.setOnClickListener(this);

        food_log_webview_layout.setVisibility(View.GONE);
        get_log_layout.setVisibility(View.VISIBLE);
        view_log_layout.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.get_food:
                String date = food_log_date.getText().toString();
                food_log_webview_layout.setVisibility(View.VISIBLE);
                getFoodInfo(date);
                get_log_layout.setVisibility(v.GONE);
                view_log_layout.setVisibility(v.VISIBLE);
                food_log_webview_layout.setVisibility(View.GONE);
                break;
            case R.id.food_log_back2:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }

    private void getFoodInfo(String date){
        String url = FOOD_URL + date + ".json";
        fba = new FitBitAuth(food_log_webview,url);

        fba.Authorize(new GetUserJSONCallback() {
            @Override
            public void done(JSONObject jObject) {
                Log.i("Authorize", "Food Log: " + jObject);
                if (jObject == null) {
                } else {
                    setFoodData(jObject);
                }

            }
        });
    }

    private void setFoodData(JSONObject jObject){
        String calories, carbs, fat, fiber, protein, sodium, water, calories_goal;
        try {
            JSONObject food_summary = jObject.getJSONObject("summary");
            calories = food_summary.getString("calories"); log_calories.setText(calories);
            carbs = food_summary.getString("carbs"); log_carbs.setText(carbs);
            fat = food_summary.getString("fat"); log_fat.setText(fat);
            fiber = food_summary.getString("fiber"); log_fiber.setText(fiber);
            protein = food_summary.getString("protein"); log_protein.setText(protein);
            sodium = food_summary.getString("sodium"); log_sodium.setText(sodium);
            water = food_summary.getString("water"); log_water.setText(water);

            JSONObject goals = jObject.getJSONObject("goals");
            calories_goal = goals.getString("calories"); log_calories_goal.setText(calories_goal);

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
