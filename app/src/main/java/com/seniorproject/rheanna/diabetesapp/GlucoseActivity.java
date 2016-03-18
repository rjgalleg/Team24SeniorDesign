package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GlucoseActivity extends AppCompatActivity implements View.OnClickListener {
    private DatePicker datePicker;
    private NumberPicker numberPicker;
    private Button general_button, fasting_button, before_button, after_button, save_button, back_button;
    private Spinner medication_spinner;
    private EditText hba1c, glucose_date;
    private String tag;
    private String glucose_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose);

        //datePicker = (DatePicker) findViewById(R.id.datePicker);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        general_button = (Button) findViewById(R.id.general_button);
        fasting_button = (Button)findViewById(R.id.fasting_button);
        before_button = (Button) findViewById(R.id.before_button);
        after_button = (Button) findViewById(R.id.after_button);
        save_button = (Button) findViewById(R.id.save_button);
        back_button = (Button) findViewById(R.id.cancel_button);
        medication_spinner = (Spinner) findViewById(R.id.medication_spinner);
        hba1c = (EditText) findViewById(R.id.hba1c);
        glucose_date = (EditText) findViewById(R.id.glucose_date);

        general_button.setOnClickListener(this);
        fasting_button.setOnClickListener(this);
        after_button.setOnClickListener(this);
        before_button.setOnClickListener(this);
        save_button.setOnClickListener(this);
        back_button.setOnClickListener(this);

        List<String> items = new ArrayList<String>();
        items.add("None");
        items.add("Taken");
        items.add("Not Taken");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medication_spinner.setAdapter(adapter);
        numberPicker.setMaxValue(200);
        numberPicker.setMinValue(0);
        //numberPicker.setDisplayedValues(values);

        NumberPicker.OnValueChangeListener onValueChanged =new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                glucose_number = String.valueOf(newVal);
            }
        };
        numberPicker.setOnValueChangedListener(onValueChanged);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.general_button:
                tag = "general";
                break;
            case R.id.fasting_button:
                tag = "fasting";
                break;
            case R.id.before_button:
                tag = "before";
                break;
            case R.id.after_button:
                tag = "after";
                break;
            case R.id.save_button:
                storeGlucose();
                Toast.makeText(GlucoseActivity.this, "Data Stored", Toast.LENGTH_LONG).show();
                break;
            case R.id.cancel_button:
                startActivity(new Intent(this, GlucoseMenu.class));
                break;
        }
    }

    public void storeGlucose(){
        //String date = datePicker.toString();
        //final String[] number;
        String HBA1C = hba1c.getText().toString();
        String date = glucose_date.getText().toString();
        String medication = String.valueOf(medication_spinner.getSelectedItem());
        UserData userdata = new UserData("rjgalleg", date, glucose_number, tag, medication, HBA1C);
        Log.i("Authorize", "date: "+date +", medication: "+medication+", HBA1C: "+HBA1C+", glucose number: "+glucose_number +"tag: "+tag);
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserGlucoseInBackground(userdata, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(GlucoseActivity.this, GlucoseActivity.class));
            }
        });


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
