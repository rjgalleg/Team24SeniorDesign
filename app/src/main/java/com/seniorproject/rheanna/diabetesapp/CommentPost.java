package com.seniorproject.rheanna.diabetesapp;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

public class CommentPost extends AppCompatActivity implements View.OnClickListener {

    Button comment_button;

    EditText commentedittext;

    UserLocalStore userLocalStore;

    CommentLocalStore commentLocalStore;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_post);

        userLocalStore = new UserLocalStore(this);

        commentLocalStore= new CommentLocalStore(this);

        commentedittext = (EditText) findViewById(R.id.comment);

        comment_button = (Button) findViewById(R.id.comment_button);

        comment_button.setOnClickListener(this);

    }

    //Switches Between Button Clicks

    @Override

    public void onClick(View v) {

        switch(v.getId()){

            case R.id. comment_button:

                String url_string="/CommentPost.php";

                User user = userLocalStore.getLoggedInUser();

                String comment = commentedittext.getText().toString();

                commentPost(url_string, user.username, comment,

                        commentLocalStore.getTitle(), commentLocalStore.getCommetUserName());

                startActivity(new Intent(this, CommentNewsFeed.class));

                break;

        }

    }

    private void commentPost(String url_string, String originalusername, String

            comment, String title, String commentusername){

        CommentServerResquests CommentServerRequests = new

                CommentServerResquests(this);

        CommentServerRequests.storeUserDataInBackground(url_string, originalusername,

                comment, title, commentusername) ;

    }

}