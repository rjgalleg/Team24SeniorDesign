package com.seniorproject.rheanna.diabetesapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.*;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rheanna on 1/18/2016.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIME = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://seniorproject.comxa.com";
    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback callback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, callback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callback){
        progressDialog.show();
        new FetchUserDataAsyncTask(user, callback).execute();
    }

    public void storeUserSleepDataInBackground(UserData userData, GetUserCallback callback){
        progressDialog.show();
        new StoreUserSleepDataAsyncTask(userData, callback).execute();
    }

    public void fetchUserSleepDataInBackground(String username, GetUserDataCallback callback){
        //progressDialog.show();
        new FetchUserSleepDataAsyncTask(username, callback).execute();
    }

    public void storeUserHappyDataInBackground(UserData userData, GetUserCallback callback){
        progressDialog.show();
        new StoreUserHappyDataAsyncTask(userData, callback).execute();
    }

    public void fetchUserHappyDataInBackground(String username, GetUserDataCallback callback){
        new FetchUserHappyDataAsyncTask(username, callback).execute();
    }

    public void storeUserGlucoseInBackground(UserData userData, GetUserCallback callback){
        new StoreUserGlucoseAsyncTask(userData, callback).execute();
    }

    public void fetchUserGlucoseInBackground(String date, GetUserDataCallback callback){
        new FetchUserGlucoseAsyncTask(date, callback).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void>{
        User user;
        GetUserCallback callback;

        public StoreUserDataAsyncTask(User user, GetUserCallback callback){
            this.user = user;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params){
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.name));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);
            super.onPostExecute(aVoid);
        }

    }

    public class StoreUserSleepDataAsyncTask extends AsyncTask<Void, Void, Void>{
        UserData userData;
        GetUserCallback callback;
        public StoreUserSleepDataAsyncTask(UserData userData, GetUserCallback callback){
            this.userData = userData;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", userData.username));
            dataToSend.add(new BasicNameValuePair("date", userData.date));
            dataToSend.add(new BasicNameValuePair("hours", userData.hours));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/SleepLog.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);
            super.onPostExecute(aVoid);
        }
    }



    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback callback;

        public FetchUserDataAsyncTask(User user, GetUserCallback callback) {
            this.user = user;
            this.callback = callback;
        }
        User returnedUser;
        @Override
        protected User doInBackground(Void... params) {

            try {
                URL url = new URL(SERVER_ADDRESS + "/Login.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user.username, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(user.password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine() ) != null){
                    response += line;
                }
                bufferedReader.close();
            } catch(Exception e){
                e.printStackTrace();
            }
            return user;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            callback.done(returnedUser);
            super.onPostExecute(user);
        }

    }

    public class FetchUserSleepDataAsyncTask extends AsyncTask<Void, Void, JSONArray> {
        String username;
        GetUserDataCallback callback;
        JSONArray jarray;

        public FetchUserSleepDataAsyncTask(String username, GetUserDataCallback callback) {
            this.username = username;
            this.callback = callback;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", username));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/FetchSleepData.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                String result = EntityUtils.toString(httpResponse.getEntity());
                result = '[' + result + ']';
                result = result.replace("}{", "},{");
                jarray = new JSONArray(result);
                if(jarray.length() == 0){
                    return null;
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return jarray;

        }

        @Override
        protected void onPostExecute(JSONArray jarray) {
            progressDialog.dismiss();
            callback.done(jarray);
            super.onPostExecute(jarray);
        }


    }


    public class StoreUserHappyDataAsyncTask extends AsyncTask<Void, Void, Void>{
        UserData userData;
        GetUserCallback callback;

        public StoreUserHappyDataAsyncTask(UserData userData, GetUserCallback callback){
            this.userData = userData;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", userData.username));
            dataToSend.add(new BasicNameValuePair("date", userData.date));
            dataToSend.add(new BasicNameValuePair("value", userData.hours));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/HappyLog.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                //client.execute(post);
                client.execute(post);

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class FetchUserHappyDataAsyncTask extends AsyncTask<Void, Void, JSONArray> {
        String username;
        GetUserDataCallback callback;
        JSONArray jarray;

        public FetchUserHappyDataAsyncTask(String username, GetUserDataCallback callback) {
            this.username = username;
            this.callback = callback;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", username));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/FetchUserHappyData.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                String result = EntityUtils.toString(httpResponse.getEntity());
                result = '[' + result + ']';
                result = result.replace("}{", "},{");
                jarray = new JSONArray(result);
                if(jarray.length() == 0){
                    return null;
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return jarray;

        }

        @Override
        protected void onPostExecute(JSONArray jarray) {
            progressDialog.dismiss();
            callback.done(jarray);
            super.onPostExecute(jarray);
        }


    }

    public class StoreUserGlucoseAsyncTask extends AsyncTask<Void, Void, Void>{
        UserData userData;
        GetUserCallback callback;

        public StoreUserGlucoseAsyncTask(UserData userData, GetUserCallback callback){
            this.userData = userData;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", userData.username));
            dataToSend.add(new BasicNameValuePair("date", userData.date));
            dataToSend.add(new BasicNameValuePair("number", userData.number));
            dataToSend.add(new BasicNameValuePair("tag", userData.tag));
            dataToSend.add(new BasicNameValuePair("medication", userData.medication));
            dataToSend.add(new BasicNameValuePair("HBA1C", userData.HBA1C));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/StoreGlucoseData.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                //client.execute(post);
                client.execute(post);

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class FetchUserGlucoseAsyncTask extends AsyncTask<Void, Void, JSONArray> {
        String date;
        GetUserDataCallback callback;
        JSONArray jarray;

        public FetchUserGlucoseAsyncTask(String date, GetUserDataCallback callback) {
            this.date = date;
            this.callback = callback;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("date", date));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/FetchUserGlucoseData.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                String result = EntityUtils.toString(httpResponse.getEntity());
                result = '[' + result + ']';
                result = result.replace("}{", "},{");
                jarray = new JSONArray(result);
                if(jarray.length() == 0){
                    return null;
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return jarray;

        }

        @Override
        protected void onPostExecute(JSONArray jarray) {
            progressDialog.dismiss();
            callback.done(jarray);
            super.onPostExecute(jarray);
        }


    }

}
