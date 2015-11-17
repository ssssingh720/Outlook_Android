package com.app.outlook.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.app.outlook.listener.ServerCallback;
import com.app.outlook.manager.SessionManager;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.BaseVO;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.modal.ResponseError;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class RequestManager<T> {

    public static final boolean IS_LIVE = true;

    public static final String TESTING_SERVER = "http://52.76.121.132/";
    //    public static final String TESTING_SERVER = "http://outlook.jumpcatch.com/";
    public static final String LIVE_SERVER = "http://52.76.121.132/";
    //    public static final String LIVE_SERVER = "http://outlook.jumpcatch.com/";
    public static final String VERSION = "v1";

    public static final String TEST_BASEFEED_URL = TESTING_SERVER;
    public static final String LIVE_BASEFEED_URL = LIVE_SERVER;


    public static RequestManager instance;
    protected String LOG_TAG = "RequestManager";
    private Context mCtx;
    private RequestQueue mRequestQueue;
    private ArrayList<GsonRequest<T>> requestArr = new ArrayList<GsonRequest<T>>();
    private Context mContext;
    private String baseFeedURL;

    /*
     * Constructor
     */
    public RequestManager(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        baseFeedURL = "";
        if (RequestManager.IS_LIVE)
            baseFeedURL = LIVE_BASEFEED_URL;
        else
            baseFeedURL = TEST_BASEFEED_URL;

    }

    /*
     * Singleton Initialization
     */
    public static RequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new RequestManager(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.

            DefaultHttpClient mDefaultHttpClient = new DefaultHttpClient();

            final ClientConnectionManager mClientConnectionManager = mDefaultHttpClient.getConnectionManager();
            final HttpParams mHttpParams = mDefaultHttpClient.getParams();
            final ThreadSafeClientConnManager mThreadSafeClientConnManager = new ThreadSafeClientConnManager(mHttpParams, mClientConnectionManager.getSchemeRegistry());
            mDefaultHttpClient = new DefaultHttpClient(mThreadSafeClientConnManager, mHttpParams);
            final HttpStack httpStack = new HttpClientStack(mDefaultHttpClient);


            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), httpStack);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    /*
     * Method to create the request from the passed parameters and place a Volley request
     */
    public void placeRequest(String methodName, Class<T> clazz, ServerCallback<T> listener, HashMap<String, String> feedParams, boolean isPOST) {

        String feedurl = baseFeedURL.concat(methodName);
        String urlParam = "?";

        if (!isPOST && feedParams != null) {
            feedurl = createGetWithParams(feedurl, feedParams);
            urlParam = "&";
        }
        // Check if the feedParams has Authkey if so then get it again from
        // the SharedPreferences
        if (feedParams != null && feedParams.containsKey(FeedParams.AUTHKEY)) {
            feedParams.put(FeedParams.AUTHKEY,
                    SessionManager.getSessionId(mCtx));
        }


        try {
            if(SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID) != null) {
                feedurl = feedurl + urlParam + FeedParams.USER_ID + "=" + SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID)
                        + "&" + FeedParams.TOKEN + "=" + URLEncoder.encode(SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Log.d("Requestmanager", "SearchURL::" + feedurl);
        GsonRequest<T> jsObjRequest = new GsonRequest<T>(isPOST ? Method.POST : Method.GET,
                feedurl,
                methodName,
                clazz,
                feedParams,
                listener,
                createSuccessListener(),
                createErrorListener());

        if (SessionManager.isUserSessionValid(mCtx)) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put(FeedParams.HEADERS_AUTHENTICATION, "token " + SessionManager.getSessionId(mCtx));
            jsObjRequest.setHeaders(headers);
        }

http://52.76.121.132/get-issues/?mag_id=0&year=2015&month=1&user_id=5&token=rajendra@inkoniq.com|1446873092|dU73W1qQDCOhfQn4N0XFvp923woZeq6k1eBxyYSC5kg|93d274e078f9a404ce19dc355750c62865a7489f510ab815121bfdb38e9308d6
        jsObjRequest.setTag("GN");
        int socketTimeout = 60000;//60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        addToRequestQueue(jsObjRequest);
    }

    private String createGetWithParams(String url, HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value != null) {
                try {
                    value = URLEncoder.encode(String.valueOf(value), HTTP.UTF_8);
                    if (builder.length() > 0)
                        builder.append("&");
                    builder.append(key).append("=").append(value);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }

        return (url += "?" + builder.toString());
    }


    /*
     * Method to create the request from the passed parameters and place a Volley request
     */
    public void placeRequest(String methodName, Class<T> clazz, ServerCallback<T> listener, JSONObject jsonObj) {

        String feedurl = baseFeedURL.concat(methodName);

        JSONRequest<T> jsObjRequest = new JSONRequest<T>(Method.POST,
                feedurl,
                methodName,
                clazz,
                jsonObj,
                listener,
                createSuccessListener(),
                createErrorListener());

        if (SessionManager.isUserSessionValid(mCtx)) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put(FeedParams.HEADERS_AUTHENTICATION, "token " + SessionManager.getSessionId(mCtx));
            jsObjRequest.setHeaders(headers);
        }


        jsObjRequest.setTag("GN");
        int socketTimeout = 30000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);


        addToRequestQueue(jsObjRequest);

    }


    /*
     * Method to create the request from the passed parameters and place a Volley request
     */
    public void placeMultiPartRequest(String methodName, Class<T> clazz, ServerCallback<T> listener, HashMap<String, String> feedParams, File file, String fileKey) {

        String feedurl = baseFeedURL.concat(methodName);

        // Check if the feedParams has Authkey if so then get it again from
        // the SharedPreferences
        if (feedParams != null && feedParams.containsKey(FeedParams.AUTHKEY)) {
            feedParams.put(FeedParams.AUTHKEY,
                    SessionManager.getSessionId(mCtx));
        }

        // Before making the call.. Check if the Network is available or not
//		if (!isNetworkAvailable(mCtx)) {
//			result.setStatus(ERR);
//			result.setStatusCode(ErrorCodeConsts.NETOWORK_UNAVAILABLE);
//			result.setErrorMessage(Consts.NO_NETWORK);
//			return result;
//		}

        MultiPartRequest jsObjRequest = new MultiPartRequest<T>(Method.POST,
                feedurl,
                methodName,
                clazz,
                feedParams,
                listener,
                createSuccessListener(),
                createErrorListener(), fileKey, file);

        if (SessionManager.isUserSessionValid(mCtx)) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put(FeedParams.HEADERS_AUTHENTICATION, "token " + SessionManager.getSessionId(mCtx));
            jsObjRequest.setHeaders(headers);
        }


        jsObjRequest.setTag("GN");
        int socketTimeout = 30000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);


        addToRequestQueue(jsObjRequest);

    }


    private IQListener<T> createSuccessListener() {
        return new IQListener<T>() {

            @Override
            public void onResponse(T response, GsonRequest<T> request) {
                try {
                    Log.i(LOG_TAG, request.getApiMethodName() + ": onResponse called");
                    if (((BaseVO) response).getError() == null || ((BaseVO) response).getError().equalsIgnoreCase("false"))
                        request.getSourceListener().onAPIResponse(response, request.getApiMethodName());
                    else {
                        ResponseError errorResponse = new ResponseError();
                        errorResponse.setErrorMessage(((BaseVO) response).getError());
                        request.getSourceListener().onErrorResponse(errorResponse, request.getApiMethodName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(T response, MultiPartRequest<T> request) {
                try {
                    Log.i(LOG_TAG, request.getApiMethodName() + ": onResponse called");
                    request.getSourceListener().onAPIResponse(response, request.getApiMethodName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(T response, JSONRequest<T> request) {
                try {
                    Log.i(LOG_TAG, request.getApiMethodName() + ": onResponse called");
                    request.getSourceListener().onAPIResponse(response, request.getApiMethodName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error, GsonRequest<T> request) {
                try {
                    Log.i(LOG_TAG, request.getApiMethodName() + ": onErrorResponse called");
                    request.getSourceListener().onErrorResponse(error, request.getApiMethodName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error, JSONRequest request) {
                try {
                    Log.i(LOG_TAG, request.getApiMethodName() + ": onErrorResponse called");
                    request.getSourceListener().onErrorResponse(error, request.getApiMethodName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(T response) {
                // TODO Auto-generated method stub

            }


        };
    }

    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("GN", "RequestManager : VolleyError");
//                String errorMsg = VolleyErrorHelper.getMessage(error, getActivity());
//                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        };
    }

    /*
     * Method to stop the requests currently in the queue
	 */
    public void stop() {
        mRequestQueue.stop();

    }

    /*
     * Method to start processing the requests in the queue
     */
    public void start() {
        mRequestQueue.start();

    }

    /*
     * Method to cancel all the requests in the queue
     */
    public void cancelAll() {
        requestArr.clear();
        mRequestQueue.cancelAll("EA");
    }


    /**
     * Customized Callback interface for delivering parsed responses.
     */
    public interface IQListener<T> extends Response.Listener<T> {
        /**
         * Called when a response is received.
         */
        public void onResponse(T response, GsonRequest<T> request);

        public void onResponse(T response, MultiPartRequest<T> request);

        public void onResponse(T response, JSONRequest<T> request);

        public void onErrorResponse(VolleyError error, GsonRequest<T> request);

        void onErrorResponse(VolleyError error, JSONRequest jsonRequest);
    }


}
