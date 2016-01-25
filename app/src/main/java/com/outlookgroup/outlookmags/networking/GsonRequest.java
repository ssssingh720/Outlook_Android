package com.outlookgroup.outlookmags.networking;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.outlookgroup.outlookmags.listener.ServerCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/*
 * Class which handles making the Volley Requests and delivering the data through GSON
 */

public class GsonRequest<T> extends Request<T> {

    private static final int EA_SOCKET_TIMEOUT_MS = 1000 * 10;
    private final Class<T> clazz;
    private final Map<String, String> params;
    private final RequestManager.IQListener<T> listener;
    private Gson gson;
    private Map<String, String> headers;
    private ServerCallback<T> sourceListener;
    private String apiMethodName;
    private int isPost;

    public GsonRequest(int method, String url, String apiMethodName, Class<T> clazz,
                       Map<String, String> params, ServerCallback<T> sourceListener, RequestManager.IQListener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(
                EA_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.setSourceListener(sourceListener);
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Declare all custom Deserializers here

        //
        this.isPost = method;
        this.gson = gsonBuilder.create();
        this.setApiMethodName(apiMethodName);
        this.clazz = clazz;
        this.params = params;
        this.listener = listener;
        this.headers = null;
        //gson = new Gson();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    protected HashMap<String, String> getParams() throws AuthFailureError {
        return (HashMap<String, String>) params;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response, this);
    }

    @Override
    public void deliverError(VolleyError error) {
        listener.onErrorResponse(error, this);

    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Log.d("JsonSyntaxException", "ASDASD");
            return Response.error(new ParseError(e));
        }
    }

    /**
     * @return the sourceListener
     */
    public ServerCallback getSourceListener() {
        return sourceListener;
    }

    /**
     * @param sourceListener the sourceListener to set
     */
    public void setSourceListener(ServerCallback<T> sourceListener) {
        this.sourceListener = sourceListener;
    }

    /**
     * @return the apiMethodName
     */
    public String getApiMethodName() {
        return apiMethodName;
    }

    /**
     * @param apiMethodName the apiMethodName to set
     */
    public void setApiMethodName(String apiMethodName) {
        this.apiMethodName = apiMethodName;
    }


    public int getIsPost() {
        return isPost;
    }

    /**
     * @return the clazz
     */
    public Class getClazz() {
        return clazz;
    }
}