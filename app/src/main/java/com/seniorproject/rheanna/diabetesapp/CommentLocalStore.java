package com.seniorproject.rheanna.diabetesapp;


import android.content.Context;

import android.content.SharedPreferences;

/**

 * Created by Victor on 3/7/2016.

 */

public class CommentLocalStore {

    public static final String SP_NAME = "commentDetails"; //FileName to store userinfo

    SharedPreferences userLocalDatabase;                //Store data on the phone

    public CommentLocalStore(Context context){

        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);

    }

    public void storeCommentData(String userpost, String title, String

            commetusername){

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();

        spEditor.putString("userpost", userpost);

        spEditor.putString("title", title);

        spEditor.putString("commetusername", commetusername);

        spEditor.commit();

    }

    public String getuserpost(){

        String userpost = userLocalDatabase.getString("userpost", "");

        return userpost;

    }

    public String getTitle(){

        String title = userLocalDatabase.getString("title", "");

        return title;

    }

    public String getCommetUserName(){

        String commetusername = userLocalDatabase.getString("commetusername", "");

        return commetusername;

    }

    public void clearUserData(){

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();

        spEditor.clear();

        spEditor.commit();

    }

}