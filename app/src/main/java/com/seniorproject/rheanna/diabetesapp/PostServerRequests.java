package com.seniorproject.rheanna.diabetesapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 2/17/2016.
 */
public class PostServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://seniorproject.comxa.com";
    String JSON_STRING;

    public PostInterface delegate = null;

    public PostServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait..");
    }

    public void storeUserDataInBackground(String url_string, String username, String post, String title) {
        progressDialog.show();
        new StoreUserDataAsyncTask1(url_string,username,post,title).execute();
    }

    public void fetchUserDataInBackground(String url_string) {
        progressDialog.show();

        new FetchUserDataAsyncTask2(url_string).execute();

    }

    public class StoreUserDataAsyncTask1 extends AsyncTask<Void, Void, Void> {

        String url_string, username2, post, title;

        public StoreUserDataAsyncTask1(String url_string,String username, String post, String title) {
            this.url_string=url_string;
            this.username2 = username;
            this.post= post;
            this.title=title;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(SERVER_ADDRESS+url_string);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", username2)
                        .appendQueryParameter("title", title)
                        .appendQueryParameter("userpost", post);
                String query = builder.build().getEncodedQuery();
                //Log.d("query", query);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                int code = conn.getResponseCode();
                //Log.d("code", Integer.toString(code));
                conn.connect();
            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class FetchUserDataAsyncTask2 extends AsyncTask<Void, Void, String> {


        String url_string;
        public FetchUserDataAsyncTask2(String url_string) {
            this.url_string=url_string;
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                URL url = new URL(SERVER_ADDRESS+url_string);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                return stringBuilder.toString();

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            delegate.processFinish(result);
        }

    }
}

