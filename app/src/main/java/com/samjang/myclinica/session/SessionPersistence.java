package com.samjang.myclinica.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.samjang.myclinica.model.User;

import java.util.Date;

public class SessionPersistence {

    private static final String PREF_NAME = "Session";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionPersistence(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Save login user session
     @param email
     @param firstName
     */
    public void loginUser(String email, String firstName) {
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_FIRST_NAME, firstName);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setFirstName(mPreferences.getString(KEY_FIRST_NAME, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }

}
