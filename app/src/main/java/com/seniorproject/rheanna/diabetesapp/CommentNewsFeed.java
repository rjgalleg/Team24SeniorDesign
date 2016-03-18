package com.seniorproject.rheanna.diabetesapp;
        import android.content.Intent;

        import android.os.Bundle;

        import android.support.design.widget.FloatingActionButton;

        import android.support.design.widget.Snackbar;

        import android.support.v7.app.AppCompatActivity;

        import android.support.v7.widget.Toolbar;

        import android.view.View;

        import android.widget.AdapterView;

        import android.widget.EditText;

        import android.widget.ListAdapter;

        import android.widget.ListView;

        import android.widget.TextView;

        import org.json.JSONArray;

        import org.json.JSONException;

        import org.json.JSONObject;

        import java.util.ArrayList;

        import java.util.Collections;

public class CommentNewsFeed extends AppCompatActivity implements CommentInterface {

    CommentLocalStore commentLocalStore;

    JSONArray jsonArray;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_news_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        commentLocalStore= new CommentLocalStore(this);

        TextView titlebox = (TextView) findViewById(R.id.titlebox);

        TextView postbox = (TextView) findViewById(R.id.postbox);

        titlebox.setText(commentLocalStore.getTitle());

        postbox.setText(commentLocalStore.getuserpost());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab7);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(CommentNewsFeed.this, CommentPost.class);

                startActivity(intent);

            }

        });

        String url_string="/CommentFetchPost.php";

        fetchjson(url_string,commentLocalStore.getTitle());

    }

    @Override

    public void processFinish(String output) {

        try {

            ListView myListView = (ListView) findViewById(R.id.listView2);

            ListAdapter myAdapter;

            jsonArray = new JSONArray(output);

            String PostID, username, title, comment,usernamecomment;

            final ArrayList<UserCommentInfo> listItems = new

                    ArrayList<UserCommentInfo>();

            for(int count=0;count < jsonArray.length();count=count+5){

                PostID = jsonArray.get(count).toString();

                username = jsonArray.get(count+1).toString();

                title = jsonArray.get(count+2).toString();

                comment = jsonArray.get(count + 3).toString();

                usernamecomment = jsonArray.get(count+4).toString();

                UserCommentInfo userInfo = new

                        UserCommentInfo(PostID,username,title,comment,usernamecomment);

                listItems.add(userInfo);

            }

            //Collections.reverse(listItems);

            myAdapter = new CustomAdapter2(this, listItems);

            myListView.setAdapter(myAdapter);

        } catch (JSONException e) {

            e.printStackTrace();

        }

    }

    private void fetchjson(String url_string, String title){

        CommentServerResquests commentServerRequests = new

                CommentServerResquests(this);

        commentServerRequests.delegate=this;

        commentServerRequests.fetchUserDataInBackground(url_string, title);

    }

}