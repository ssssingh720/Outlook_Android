package com.app.outlook.networking;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.app.outlook.Utils.ServerUtil;
import com.app.outlook.listener.OnServerResponceListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


/**
 * Created by darko on 4.2.15.
 */
public class Server {
    // ==== CONSTANTS
    private static final String TAG = Server.class.getSimpleName();

    //=== INTERFACE
    static OnServerResponceListener mLoggedinListener;

    //=== HTTP Object
    private static HttpClient instance = null;

    private static String responceString;

    private static int currentMethod = -1;

    protected Server() {
        // Exists only to defeat instantiation.
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            instance = new DefaultHttpClient();
        }
        return instance;
    }

    /**
     * HTTP POST method
     */
    private static String postData(String URL, List<NameValuePair> nameValuePairs) {
        HttpPost httppost = new HttpPost(URL);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = getInstance().execute(httppost);
            responceString = EntityUtils.toString(response.getEntity());
            currentMethod = ServerUtil.METHOD_POST;
            return responceString;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HTTP GET method
     */
    private static void GETmethod(String url) {
        try {
            HttpResponse response = getInstance().execute(new HttpGet(url));
            responceString = EntityUtils.toString(response.getEntity());
            currentMethod = ServerUtil.METHOD_GET;
        } catch (Exception e) {
            Log.e(TAG, "GET METHOD Network exception", e);
        }
    }

    /**
     * Execute a server method
     *
     * @param METHOD         int that represents the method
     * @param URL            String of the url of the server
     * @param nameValuePairs a List of the nameValue pairs
     */
    public static void executeMethod(final int METHOD, final String URL, final List<NameValuePair> nameValuePairs, final Activity activity) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                switch (METHOD) {
                    case ServerUtil.METHOD_POST:
                        Log.d(TAG, postData(URL, nameValuePairs));
                        break;
                    case ServerUtil.METHOD_GET:
                        GETmethod(URL);
                        break;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mLoggedinListener = (OnServerResponceListener) activity;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responceString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mLoggedinListener.onServerResponse(jsonObject, currentMethod);
            }
        }.execute();
    }

}
