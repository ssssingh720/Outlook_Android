package com.outlookgroup.outlookmags.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Util class for storing and retrieving shared preferences. Must call {@link #} on app launch.
 */
public class SessionManager {
    public static final String SHARED_PREFS = "Outlook";
    public static final String PREF_FILE_USERSESSION = "userSession";


    // --- Permanent Information
    private static final String PREF_KEY_USERSESSION_ID = "sessionId";
    private static final String PREF_DOWNLOAD_INTERRUPTED = "PREF_DOWNLOAD_INTERRUPTED";

    private static SharedPreferences mPref;






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

    public static String getSessionId(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_ID, "");
    }

    public static void setDownloadFailed(Context context, boolean failed) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putBoolean(PREF_DOWNLOAD_INTERRUPTED, failed).apply();
    }

    public static boolean isDownloadFailed(Context context) {
        mPref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return mPref.getBoolean(PREF_DOWNLOAD_INTERRUPTED, false);
    }
}
