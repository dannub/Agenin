package com.agenin.id.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.agenin.id.Model.UserModel;
import com.google.gson.Gson;

public class UserPreference {
    private static Context context;

    public UserPreference(Context context){
        UserPreference.context = context;
    }

    private final static String PREFS_NAME = "agenin_prefs";


    public static void setUserPreference( String key, UserModel userModel) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        editor.putString(key, json);
        editor.apply();
    }

    public static UserModel getUserPreference(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        UserModel userModel = gson.fromJson(json, UserModel.class);
        return userModel;
    }

}
