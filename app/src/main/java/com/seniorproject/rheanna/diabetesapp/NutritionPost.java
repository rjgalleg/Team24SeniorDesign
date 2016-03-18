package com.seniorproject.rheanna.diabetesapp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NutritionPost extends AppCompatActivity implements View.OnClickListener {

    Button nutrition_post_button;
    EditText NTitle,NPost;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_post);

        userLocalStore = new UserLocalStore(this);

        NTitle = (EditText) findViewById(R.id.NTitle);
        NPost = (EditText) findViewById(R.id.NPost);
        nutrition_post_button= (Button) findViewById(R.id.nutrition__post_button);

        nutrition_post_button.setOnClickListener(this);
    }


    //Switches Between Button Clicks
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id. nutrition__post_button:

                String username2 =  userLocalStore.getLoggedInUser().username;
                String title = NTitle.getText().toString();
                String post = NPost.getText().toString();
                String url_string = "/NutritionPost.php";

                registerNutritionPost(url_string, username2, post, title);

                startActivity(new Intent(this, NutritionNewsFeed.class));

                break;
        }
    }

    private void registerNutritionPost(String url_string,String username, String post, String title){
        PostServerRequests PostServerRequests = new PostServerRequests(this);
        PostServerRequests.storeUserDataInBackground(url_string, username, post, title);
    }
}


