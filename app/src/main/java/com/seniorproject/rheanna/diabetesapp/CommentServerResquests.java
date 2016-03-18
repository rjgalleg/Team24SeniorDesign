package com.seniorproject.rheanna.diabetesapp;
import android.app.ProgressDialog;

import android.content.Context;

import android.net.Uri;

import android.os.AsyncTask;

import android.util.Log;

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

 * Created by Victor on 3/7/2016.

 */

public class CommentServerResquests {

    ProgressDialog progressDialog;

    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public static final String SERVER_ADDRESS = "http://seniorproject.comxa.com";

    String JSON_STRING;

    public CommentInterface delegate = null;

    public CommentServerResquests(Context context) {

        progressDialog = new ProgressDialog(context);

        progressDialog.setCancelable(false);

        progressDialog.setTitle("Processing");

        progressDialog.setMessage("Please wait..");

    }

    public void storeUserDataInBackground(String url_string, String originalusername,

                                          String comment, String title, String commentusername) {

        progressDialog.show();

        new

                StoreUserDataAsyncTask1(url_string,originalusername,comment,title,commentusername).execute();

    }

    public void fetchUserDataInBackground(String url_string, String title) {

        progressDialog.show();

        new FetchUserDataAsyncTask2(url_string,title).execute();

    }

    public class StoreUserDataAsyncTask1 extends AsyncTask<Void, Void, Void> {

        String url_string, originalusername2, comment, title, commentusername;

        public StoreUserDataAsyncTask1(String url_string,String originalusername,

                                       String comment, String title, String commentusername) {

            this.url_string=url_string;

            this.originalusername2 = originalusername;

            this.comment= comment;

            this.title=title;

            this.commentusername = commentusername;

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

                        .appendQueryParameter("username", originalusername2)

                        .appendQueryParameter("title", title)

                        .appendQueryParameter("comment", comment)

                        .appendQueryParameter("usernamecomment", commentusername);

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

        String title;

        public FetchUserDataAsyncTask2(String url_string, String title) {

            this.title=title;

            this.url_string=url_string;

        }

        @Override

        protected String doInBackground(Void... params) {

            HashMap<String, String> param = new HashMap<String, String>();

            param.put("title", title);

            try{

                URL url = new URL(SERVER_ADDRESS+url_string);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(15000);

                conn.setConnectTimeout(15000);

                conn.setRequestMethod("POST");

                conn.setDoInput(true);

                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(

                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(getPostDataString(param));

                writer.flush();

                writer.close();

                os.close();

                InputStream inputStream = new

                        BufferedInputStream(conn.getInputStream());

                BufferedReader bufferedReader = new BufferedReader(new

                        InputStreamReader(inputStream));

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

        private String getPostDataString(HashMap<String, String> params) throws

                UnsupportedEncodingException {

            StringBuilder result = new StringBuilder();

            boolean first = true;

            for (Map.Entry<String, String> entry : params.entrySet()) {

                if (first)

                    first = false;

                else

                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));

                result.append("=");

                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

            }

            return result.toString();

        }

        @Override

        protected void onPostExecute(String result) {

            progressDialog.dismiss();

            delegate.processFinish(result);

        }

    }

}