package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GlucoseMenu extends AppCompatActivity implements View.OnClickListener {
    private Button get_glucose_button, view_glucose_button, back_glucose_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose_menu);

        get_glucose_button = (Button) findViewById(R.id.get_glucose_button);
        view_glucose_button = (Button) findViewById(R.id.view_glucose_button);
        back_glucose_button = (Button) findViewById(R.id.back_glucose_button);

        get_glucose_button.setOnClickListener(this);
        view_glucose_button.setOnClickListener(this);
        back_glucose_button.setOnClickListener(this);

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
            case R.id.get_glucose_button:
                startActivity(new Intent(this, GlucoseActivity.class));
                break;
            case R.id.view_glucose_button:
                startActivity(new Intent(this, ViewGlucose.class));
                break;
            case R.id.back_glucose_button:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
