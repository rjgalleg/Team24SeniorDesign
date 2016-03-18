package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MedicalPost extends AppCompatActivity implements View.OnClickListener {

    Button medical_post_button;
    EditText MTitle,MPost;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_post);

        userLocalStore = new UserLocalStore(this);

        MTitle = (EditText) findViewById(R.id.MTitle);
        MPost = (EditText) findViewById(R.id.MPost);
        medical_post_button= (Button) findViewById(R.id.medical__post_button);

        medical_post_button.setOnClickListener(this);
    }


    //Switches Between Button Clicks
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id. medical__post_button:

                String username2 =  userLocalStore.getLoggedInUser().username;
                String title = MTitle.getText().toString();
                String post = MPost.getText().toString();
                String url_string = "/MedicalPost.php";

                registerNutritionPost(url_string, username2, post, title);

                startActivity(new Intent(this, MedicalNewsFeed.class));

                break;
        }
    }

    private void registerNutritionPost(String url_string,String username, String post, String title){
        PostServerRequests PostServerRequests = new PostServerRequests(this);
        PostServerRequests.storeUserDataInBackground(url_string, username, post, title);
    }
}




