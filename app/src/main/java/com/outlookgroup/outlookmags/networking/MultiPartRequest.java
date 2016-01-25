package com.outlookgroup.outlookmags.networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.outlookgroup.outlookmags.listener.ServerCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiPartRequest<T> extends Request<T> {

    private static final int EA_SOCKET_TIMEOUT_MS = 1000 * 10;
    private final Class<T> clazz;
    private final Map<String, String> params;
    private final RequestManager.IQListener<T> listener;
    File uploadFile;
    String fileKey;
    private Gson gson;
    private Map<String, String> headers;
    private ServerCallback<T> sourceListener;
    private String apiMethodName;
    private int isPost;
    private MultipartEntity entity = new MultipartEntity();
    /* To hold the parameter name and the File to upload */
    private Map<String, File> fileUploads = new HashMap<String, File>();
    /* To hold the parameter name and the string content to upload */
    private Map<String, String> stringUploads = new HashMap<String, String>();

    public MultiPartRequest(int method, String url, String methodName, Class<T> clazz, HashMap<String, String> feedParams, ServerCallback<T> sourceListener, RequestManager.IQListener<T> successListener, Response.ErrorListener listener, String fileKey, File file) {
        super(method, url, listener);
        this.setSourceListener(sourceListener);
        params = feedParams;
        this.clazz = clazz;
        this.listener = successListener;
        this.headers = null;
        this.uploadFile = file;
        this.fileKey = fileKey;
        gson = new Gson();
        buildMultipartEntity();
    }

    public MultiPartRequest(int method, String url, String apiMethodName, Class<T> clazz,
                            Map<String, String> params, ServerCallback<T> sourceListener,
                            RequestManager.IQListener<T> successListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(
                EA_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.setSourceListener(sourceListener);
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Declare all custom Deserializers here

        this.gson = gsonBuilder.create();
        this.setApiMethodName(apiMethodName);
        this.clazz = clazz;
        this.params = params;
        this.listener = successListener;
        this.headers = null;
        gson = new Gson();
        buildMultipartEntity();

    }

    @SuppressWarnings("deprecation")
    private void buildMultipartEntity() {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(uploadFile), null, null);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            InputStream in = new ByteArrayInputStream(bos.toByteArray());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        entity.addPart(fileKey, new FileBody(uploadFile));
        try {
            ArrayList<String> keySet = new ArrayList<String>(params.keySet());
            ArrayList<String> valueSet = new ArrayList<String>(params.values());
            for (int i = 0; i < params.size(); i++) {
                entity.addPart(keySet.get(i), new StringBody(valueSet.get(i)));
            }


        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
            //  entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }


    public void addFileUpload(String param, File file) {
        fileUploads.put(param, file);
    }

    public void addStringUpload(String param, String content) {
        stringUploads.put(param, content);
    }

    public Map<String, File> getFileUploads() {
        return fileUploads;
    }

    public Map<String, String> getStringUploads() {
        return stringUploads;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
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
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response, this);
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