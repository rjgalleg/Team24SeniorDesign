package com.seniorproject.rheanna.diabetesapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rheanna on 2/13/2016.
 */
public class FitBitAuth extends AppCompatActivity {
    //Constants
    public static final String CLIENT_ID = "227G7F";
    public static final String REDIRECT_URL = "https://www.fitbit.com/user/498NHZ";
    public static final String REDIRECT_URI = "https%3A%2F%2Fwww.fitbit.com%2Fuser%2F498NHZ";
    public static final String AUTHORIZATION_URL = "https://www.fitbit.com/oauth2/authorize";
    public static final String RESPONSE_TYPE_PARAM = "response_type";
    public static final String RESPONSE_TYPE_VALUE = "token";
    public static final String CLIENT_ID_PARAM = "client_id";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    public static final String SCOPE = "scope";
    public static final String SCOPE_PARAM = "heartrate%20activity%20location%20nutrition%20profile%20settings%20sleep%20social%20weight";
    public static final String QUESTION_MARK = "?";
    public static final String AMPERSAND = "&";
    public static final String EQUALS = "=";
    private static String LOG_URL = "https://api.fitbit.com/1/user/-/activities.json";
    private static String FOOD_SEARCH_URL = "https://api.fitbit.com/1/foods/search.json";

    JSONObject resultJson;
    public String authorizationToken;

    WebView webview;
    JSONObject JSONreturn;
    String input_url;
    Context context;

    public FitBitAuth(WebView webview, String input_url, Context context){
        this.webview = webview;
        this.input_url = input_url;
        this.context = context;
    }

    public FitBitAuth(WebView webview, String input_url){
        this.webview = webview;
        this.input_url = input_url;
    }

    public void Authorize(final GetUserJSONCallback callback) {

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //check for our custom callback protocol
                if (url.startsWith(REDIRECT_URL)) {
                    //user authorization is completed, and Fitbit has redirected the user to our callback URL
                    Uri uri = Uri.parse(url);
                    String fragment = uri.getFragment();
                    int index = fragment.indexOf("access_token=");
                    authorizationToken = fragment.substring(index + 13, fragment.length());
                    if(authorizationToken == null){
                        Log.i("Authorize", "The user doesn't allow authorization");
                        return true;
                    }
                    new PostRequestAsyncTask(callback).execute(input_url, authorizationToken);

                } else {
                    webview.loadUrl(url);
                    // return super.shouldOverrideUrlLoading(view, url);
                }
                return true;
            }

        });

        String authURL = getAuthorizationUrl();
        webview.loadUrl(authURL);
    }

    /**
     * Method that generates the url for get the authorization token from the Service
     * @return Url
     */
    private static String getAuthorizationUrl(){
        return AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND+CLIENT_ID_PARAM+EQUALS+CLIENT_ID
                +AMPERSAND+SCOPE+EQUALS+SCOPE_PARAM
                +AMPERSAND+REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI;
    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, JSONObject> {
        GetUserJSONCallback callback;

        public PostRequestAsyncTask(GetUserJSONCallback callback) {
            this.callback = callback;
        }
        @Override
        protected JSONObject doInBackground(String... urls) {

            if (urls.length > 0) {
                String url = urls[0];
                String authorizationToken = urls[1];
                Log.i("Authorize", "Authorization Token: " + authorizationToken);
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                httpget.setHeader("Authorization", "Bearer " + authorizationToken);

                try {
                    HttpResponse response = httpClient.execute(httpget);
                    if (response != null) {
                        //If status is OK 200
                        //Convert the string result to a JSON Object
                        if (response.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(response.getEntity());
                            //Convert the string result to a JSON Object
                            resultJson = new JSONObject(result);
                            setJsonReturn(resultJson);
                        }else{
                            String result = EntityUtils.toString(response.getEntity());
                            //Convert the string result to a JSON Object
                            JSONObject resultJson = new JSONObject(result);
                            Log.i("Authorize", "Error: "+ resultJson);
                        }
                    }

                } catch(Exception e) {
                    Log.e("Authorize", e.getLocalizedMessage());
                }
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(JSONObject jObject) {
            callback.done(jObject);
            super.onPostExecute(jObject);
        }
    };

    public void postLog(String activity, String startTime, String duration, String date, String distance, String calories, GetUserJSONCallback callback){
        new PostRequestAsyncLogTask(callback).execute(activity, startTime, duration, date, distance, calories, authorizationToken);
    }
    private class PostRequestAsyncLogTask extends AsyncTask<String, Void, JSONObject> {
        GetUserJSONCallback callback;

        public PostRequestAsyncLogTask(GetUserJSONCallback callback) {
            this.callback = callback;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            String authorizationToken = params[6];
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(LOG_URL);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(6);
            nameValuePair.add(new BasicNameValuePair("activityName", params[0]));
            nameValuePair.add(new BasicNameValuePair("startTime", params[1]));
            nameValuePair.add(new BasicNameValuePair("durationMillis", params[2]));
            nameValuePair.add(new BasicNameValuePair("date", params[3]));
            nameValuePair.add(new BasicNameValuePair("distance", params[4]));
            nameValuePair.add(new BasicNameValuePair("manualCalories", params[5]));
            httpPost.setHeader("Authorization", "Bearer " + authorizationToken);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                HttpResponse response = httpClient.execute(httpPost);
                if (response != null) {
                    //If status is OK 200
                    //Convert the string result to a JSON Object
                    String result = EntityUtils.toString(response.getEntity());
                    //Convert the string result to a JSON Object
                    JSONObject resultJson = new JSONObject(result);

                    Log.i("Authorize", "Return: "+resultJson);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return resultJson;
        }

        @Override
        protected void onPostExecute(JSONObject jObject) {
            callback.done(jObject);
            super.onPostExecute(jObject);
        }
    }

    public void postFoodLog(String query, GetUserJSONCallback callback){
        new PostRequestAsyncFoodTask(callback).execute(query, authorizationToken);
    }

    private class PostRequestAsyncFoodTask extends AsyncTask<String, Void, JSONObject> {
        GetUserJSONCallback callback;

        public PostRequestAsyncFoodTask(GetUserJSONCallback callback) {
            this.callback = callback;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            String authorizationToken = params[1];
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(FOOD_SEARCH_URL);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("query", params[0]));
            //httpPost.setHeader("Authorization", "Bearer " + authorizationToken);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                HttpResponse response = httpClient.execute(httpPost);
                if (response != null) {
                    //If status is OK 200
                    //Convert the string result to a JSON Object
                    String result = EntityUtils.toString(response.getEntity());
                    //Convert the string result to a JSON Object
                    JSONObject resultJson = new JSONObject(result);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return resultJson;
        }

        @Override
        protected void onPostExecute(JSONObject jObject) {
            callback.done(jObject);
            super.onPostExecute(jObject);
        }
    }

    public void setJsonReturn(JSONObject jr){
        JSONreturn = jr;
    }
    public JSONObject getJsonReturn(){
        return JSONreturn;
    }
}
