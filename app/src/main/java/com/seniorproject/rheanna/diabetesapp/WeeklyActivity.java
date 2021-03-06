package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class WeeklyActivity extends Activity implements View.OnClickListener {

    private Button back_button;
    private Button calories;
    private Button steps;
    private Button floors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);

        calories = (Button) findViewById(R.id.weekly_calories);
        steps = (Button) findViewById(R.id.weekly_steps);
        floors = (Button) findViewById(R.id.weekly_floors);
        back_button = (Button) findViewById(R.id.weekly_back);
        back_button.setOnClickListener(this);
        calories.setOnClickListener(this);
        floors.setOnClickListener(this);
        steps.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.weekly_back:
                startActivity(new Intent(this, Activity.class));
                break;
            case R.id.weekly_floors:
                startActivity(new Intent(this, FloorsActivity.class));
                break;
            case R.id.weekly_steps:
                startActivity(new Intent(this, WeeklySteps.class));
                break;
            case R.id.weekly_calories:
                startActivity(new Intent(this, WeeklyCalories.class));
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
