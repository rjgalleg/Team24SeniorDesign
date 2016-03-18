package com.seniorproject.rheanna.diabetesapp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FitnessPost extends AppCompatActivity implements View.OnClickListener {

    Button fitness_post_button;
    EditText FTitle,FPost;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_post);

        userLocalStore = new UserLocalStore(this);

        FTitle = (EditText) findViewById(R.id.FTitle);
        FPost = (EditText) findViewById(R.id.FPost);
        fitness_post_button= (Button) findViewById(R.id.fitness_post_button);

        fitness_post_button.setOnClickListener(this);
    }


    //Switches Between Button Clicks
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id. fitness_post_button:

                String username2 =  userLocalStore.getLoggedInUser().username;
                String title = FTitle.getText().toString();
                String post = FPost.getText().toString();
                String url_string = "/FitnessPost.php";

                registerNutritionPost(url_string, username2, post, title);
                startActivity(new Intent(this, FitnessNewsFeed.class));

                break;
        }
    }

    private void registerNutritionPost(String url_string,String username, String post, String title){
        PostServerRequests PostServerRequests = new PostServerRequests(this);
        PostServerRequests.storeUserDataInBackground(url_string, username, post, title);
    }
}



