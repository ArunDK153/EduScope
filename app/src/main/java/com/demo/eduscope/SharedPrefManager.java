package com.demo.eduscope;

import android.content.Context;
import android.content.SharedPreferences;

class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "eduscope";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    SharedPrefManager(Context context) {
        pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    void userLogin(User user) {
        editor = pref.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    boolean isLoggedIn() {
        return pref.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    User getUser() {
        return new User(
                pref.getString(KEY_ID, null),
                pref.getString(KEY_USERNAME, null),
                pref.getString(KEY_EMAIL, null)
        );
    }

    //this method will logout the user
    void logout() {
        editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
