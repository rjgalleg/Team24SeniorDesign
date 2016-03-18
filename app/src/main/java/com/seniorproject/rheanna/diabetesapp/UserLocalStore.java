package com.seniorproject.rheanna.diabetesapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rheanna on 1/14/2016.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    //used to get the shared preference
    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    //store user data on a file
    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.commit();
    }

    //get user currently logged onfrom local database
    public User getLoggedInUser(){
        String name = userLocalDatabase.getString("name", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");

        User storedUser = new User(name, username, password);
        return storedUser;
    }

    //if user is logged in, set to true, else set to false
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("loggedIn", false)==true){
            return true;
        }else{
            return false;
        }
    }
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }


}
