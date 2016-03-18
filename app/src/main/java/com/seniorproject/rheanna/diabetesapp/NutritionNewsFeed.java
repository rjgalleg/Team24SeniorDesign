package com.seniorproject.rheanna.diabetesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class NutritionNewsFeed extends AppCompatActivity  implements PostInterface{

    JSONArray jsonArray;
    CommentLocalStore commentLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_news_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        commentLocalStore = new CommentLocalStore(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NutritionNewsFeed.this, NutritionPost.class));
            }
        });

        String url_string="/NutritionFetchPost.php";
        fetchjson(url_string);
    }

    @Override
    public void processFinish(String output) {

        try {

            ListView myListView = (ListView) findViewById(R.id.listView);
            ListAdapter myAdapter;

            jsonArray = new JSONArray(output);

            int count = 0;
            String PostID, username, title, userpost;

            final ArrayList<UserPostInfo> listItems = new ArrayList<UserPostInfo>();
            while (count < jsonArray.length()) {

                JSONObject jo = jsonArray.getJSONObject(count);

                PostID = jo.getString("PostID");
                username = jo.getString("username");
                title = jo.getString("title");
                userpost = jo.getString("userpost");

                UserPostInfo userInfo = new UserPostInfo(PostID,username,title,userpost);

                listItems.add(userInfo);

                count++;
            }

            Collections.reverse(listItems);

            myAdapter = new CustomAdapter(this, listItems);
            myListView.setAdapter(myAdapter);

            myListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            UserPostInfo item = listItems.get(position);
                            //Toast.makeText(NutritionNewsFeed.this,item.title,Toast.LENGTH_LONG).show();
                            commentLocalStore.storeCommentData(item.userPost,item.title,item.userName);
                            Intent intent = new Intent(NutritionNewsFeed.this, CommentNewsFeed.class);


                            startActivity(intent);
                        }
                    }
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void fetchjson(String url_string){
        PostServerRequests postServerRequests = new PostServerRequests(this);
        postServerRequests.delegate = this;
        postServerRequests.fetchUserDataInBackground(url_string);
    }
}



