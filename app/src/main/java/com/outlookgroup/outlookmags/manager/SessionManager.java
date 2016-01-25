package com.outlookgroup.outlookmags.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Util class for storing and retrieving shared preferences. Must call {@link #initiateSession} on app launch.
 */
public class SessionManager {
    public static final int SESSION_TYPE_EMAIL = 1;
    public static final int SESSION_TYPE_FACEBOOK = 2;
    public static final int SESSION_TYPE_GPLUS = 3;
    public static final String SHARED_PREFS = "WickedRide";
    public static final String PREF_USER_CITY = "userCity";
    public static final String PREF_USER_CITY_ID = "userCityId";
    public static final String PREF_FILE_USERSESSION = "userSession";
    private static final String TAG = "StateManager";
    private static final String PREF_KEY_USERSESSION_TYPE = "sessionType";
    private static final String PREF_KEY_USERSESSION_FB_AUTHTOKEN = "sessionFbAuthToken";
    private static final String PREF_KEY_USERSESSION_FB_AUTHTOKENEXPIRES = "sessionFbAuthTokenExpires";


    // --- Permanent Information
    private static final String PREF_KEY_USERSESSION_ID = "sessionId";
    private static final String PREF_KEY_USER_PROFILE_PIC = "profile_pic";
    private static final String PREF_KEY_FB_USER_ID = "sessionId";
    private static final String PREF_KEY_GPLUS_USER_ID = "sessionId";
    private static final String PREF_KEY_USERSESSION_USERFULLNAME = "PREF_KEY_USERSESSION_USERFULLNAME";
    private static final String PREF_KEY_USERSESSION_EMAIL = "PREF_KEY_USERSESSION_EMAIL";
    private static final String PREF_KEY_USERSESSION_PASSWORD = "PREF_KEY_USERSESSION_PASSWORD";
    private static final String PREF_KEY_USER_DATE_OF_BIRTH = "PREF_KEY_USER_DATE_OF_BIRTH";
    private static final String PREF_USER_CITIES = "allCities";
    private static final String PREF_DOWNLOAD_INTERRUPTED = "PREF_DOWNLOAD_INTERRUPTED";

    private static Editor mEditor;
    private static SharedPreferences mPref;


    // TODO: bit OTT re-factor; Add generic login interface on top of different types of login

    /**
     * Save the user session to the shared preferences. WARNING: PREVIOUS SESSION WILL BE CLEARED.
     *
     * @param context
     * @param sessionType  cannot be NULL.
     * @param userFullName cannot be NULL.
     * @param email        cannot be NULL.
     * @param password     cannot be NULL.
     * @param authToken    cannot be NULL.
     */
    public static void saveUserSession(Context context, int sessionType, String userFullName,
                                       String email, String password, String authToken, String UserID,
                                       String dob, String imgUrl) {
        clearUserSession(context);
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        mEditor = mPref.edit();

        mEditor.commit();
    }


    public static void saveFacebookSession(Context context, String token) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        mEditor = mPref.edit();

        mEditor.putString(PREF_KEY_USERSESSION_FB_AUTHTOKEN, token);
        //mEditor.putLong(PREF_KEY_USERSESSION_FB_AUTHTOKENEXPIRES, expiration);

        mEditor.commit();
    }

    public static String getFacebookToken(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        return mPref.getString(PREF_KEY_USERSESSION_FB_AUTHTOKEN, "");
    }

    public static long getFacebookExpiration(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        return mPref.getLong(PREF_KEY_USERSESSION_FB_AUTHTOKENEXPIRES, 0);
    }


    /**
     * Check if the User Session in the shared preferences is valid
     *
     * @param context
     * @return True or False
     */
    public static boolean isUserSessionValid(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);

        // Keep simple for now
        return true;
    }

    /**
     * Retrieve User FirstName stored in the User Session in the shared preferences
     *
     * @param context
     * @return username
     * @throws NullPointerException
     */
    public static String getUserFullName(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_USERFULLNAME, null);
    }

    /**
     * Retrieve User Email stored in the User Session in the shared preferences
     *
     * @param context
     * @return username
     * @throws NullPointerException
     */
    public static String getUserEmail(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_EMAIL, null);
    }


    /**
     * Retrieve Password stored in the User Session in the shared preferences
     *
     * @param context
     * @return password
     * @throws NullPointerException
     */
    public static String getUserPassword(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_PASSWORD, null);
    }

    /**
     * Retrieve Session Type stored in the User Session in the shared preferences
     *
     * @param context
     * @return sessionType
     */
    public static int getUserSessionType(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getInt(PREF_KEY_USERSESSION_TYPE, 0);
    }

    public static String getUserImageUrl(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USER_PROFILE_PIC, "");
    }


    /**
     * Clear the user session available in the shared preferences. Removes the Username, Password and StartDate of current session
     *
     * @param context
     */
    public static void clearUserSession(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        mEditor.remove(PREF_KEY_USERSESSION_ID);
        mEditor.remove(PREF_KEY_USERSESSION_USERFULLNAME);
        mEditor.remove(PREF_KEY_USERSESSION_EMAIL);
        mEditor.remove(PREF_KEY_USERSESSION_PASSWORD);
        mEditor.remove(PREF_KEY_USERSESSION_TYPE);
        mEditor.remove(PREF_KEY_USERSESSION_FB_AUTHTOKEN);
        mEditor.remove(PREF_KEY_USERSESSION_FB_AUTHTOKENEXPIRES);

        mEditor.commit();
        Log.d(TAG, "StateManager: clearUserSession: SESSION CLEARED");
    }


    /**
     * To prevent null pointer exceptions while calling the preference file when it doesn't exist.
     * If a solution is found to avoid this behavior, this method should be removed and the code refactored.
     * #initiateSession
     *
     * @param context
     */
    public static void initiateSession(Context context) {
        mEditor =
                context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE).edit();
        mEditor.commit();
        Log.d(TAG, "StateManager: initiateSession: SESSION INITIATED");
    }

    public static String getSessionId(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_ID, "");
    }

    public static void saveAuthToken(Context context, String authToken) {

        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        mEditor.putString(PREF_KEY_USERSESSION_ID, authToken);
        mEditor.commit();

    }

    public static String getUserCity(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_USER_CITY, null);
    }

    public static int getUserCityID(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getInt(PREF_USER_CITY_ID, -1);
    }

    public static void setUserCity(Context context, String city) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_USER_CITY, city).commit();
    }

    public static void setUserCityID(Context context, int cityId) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putInt(PREF_USER_CITY_ID, cityId).commit();
    }

    public static void setAllCities(Context context, String allCities) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_USER_CITIES, allCities).commit();
    }

    public static String getAllCities(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_USER_CITIES, null);
    }

    public static void setDownloadFailed(Context context, boolean failed) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putBoolean(PREF_DOWNLOAD_INTERRUPTED, failed).apply();
    }

    public static boolean isDownloadFailed(Context context) {
        mPref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return mPref.getBoolean(PREF_DOWNLOAD_INTERRUPTED, false);
    }
}
