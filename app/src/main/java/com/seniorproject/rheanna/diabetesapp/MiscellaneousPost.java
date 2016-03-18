package com.seniorproject.rheanna.diabetesapp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MiscellaneousPost extends AppCompatActivity implements View.OnClickListener{

    Button miscellaneous_post_button;
    EditText MISTitle,MISPost;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscellaneous_post);

        userLocalStore = new UserLocalStore(this);

        MISTitle = (EditText) findViewById(R.id.MISTitle);
        MISPost = (EditText) findViewById(R.id.MISPost);
        miscellaneous_post_button= (Button) findViewById(R.id.miscellaneous__post_button);

        miscellaneous_post_button.setOnClickListener(this);
    }


    //Switches Between Button Clicks
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id. miscellaneous__post_button:

                String username2 =  userLocalStore.getLoggedInUser().username;
                String title = MISTitle.getText().toString();
                String post = MISPost.getText().toString();
                String url_string = "/MiscellaneousPost.php";

                registerNutritionPost(url_string, username2, post, title);
                startActivity(new Intent(this, MiscellaneousNewsFeed.class));

                break;
        }
    }

    private void registerNutritionPost(String url_string,String username, String post, String title){
        PostServerRequests PostServerRequests = new PostServerRequests(this);
        PostServerRequests.storeUserDataInBackground(url_string, username, post, title);
    }
}


