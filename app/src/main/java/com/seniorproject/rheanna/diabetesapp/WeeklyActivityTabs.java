package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class WeeklyActivityTabs extends AppCompatActivity {
    private TabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_activity_tabs);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec spec1 = mTabHost.newTabSpec("Calories");
        TabHost.TabSpec spec2 = mTabHost.newTabSpec("Steps");
        TabHost.TabSpec spec3 = mTabHost.newTabSpec("Floors");

        spec1.setIndicator("Calories");
        spec2.setIndicator("Steps");
        spec3.setIndicator("Floors");

        spec1.setContent(new Intent(this, WeeklyCalories.class));
        spec2.setContent(new Intent(this, WeeklySteps.class));
        spec3.setContent(new Intent(this, FloorsActivity.class));

        mTabHost.addTab(spec1);
        mTabHost.addTab(spec2);
        mTabHost.addTab(spec3);
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
