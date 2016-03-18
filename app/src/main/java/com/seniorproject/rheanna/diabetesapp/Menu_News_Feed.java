package com.seniorproject.rheanna.diabetesapp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Menu_News_Feed extends AppCompatActivity implements View.OnClickListener {

    Button health_button, nutrition_button, medical_button, miscellaneous_button, fitness_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__news__feed);

        ImageView NimageView = (ImageView) findViewById(R.id.imageView1);
        NimageView.setImageResource(R.drawable.n);

        ImageView NimageView2 = (ImageView) findViewById(R.id.imageView2);
        NimageView2.setImageResource(R.drawable.med);
        ImageView NimageView3 = (ImageView) findViewById(R.id.imageView3);
        NimageView3.setImageResource(R.drawable.fit);
        ImageView NimageView4 = (ImageView) findViewById(R.id.imageView4);
        NimageView4.setImageResource(R.drawable.quest);

        nutrition_button = (Button) findViewById(R.id.nutrition_button);
        medical_button = (Button) findViewById(R.id.medical_button);
        fitness_button = (Button) findViewById(R.id.fitness_button);
        miscellaneous_button = (Button) findViewById(R.id.miscellaneous_button);

        nutrition_button.setOnClickListener(this);
        medical_button.setOnClickListener(this);
        fitness_button.setOnClickListener(this);
        miscellaneous_button.setOnClickListener(this);

    }

    //Switches Between Button Clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nutrition_button:
                startActivity(new Intent(this, NutritionNewsFeed.class));
                break;
            case R.id.medical_button:
                startActivity(new Intent(this, MedicalNewsFeed.class));
                break;
            case R.id.fitness_button:
                startActivity(new Intent(this, FitnessNewsFeed.class));
                break;
            case R.id.miscellaneous_button:
                startActivity(new Intent(this, MiscellaneousNewsFeed.class));
                break;
        }
    }

}