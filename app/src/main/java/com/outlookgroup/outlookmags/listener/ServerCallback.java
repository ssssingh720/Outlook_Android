/*
 * Copyright (c) 2014 Inkoniq
 * All Rights Reserved.
 * @since July 20, 2014 
 * @author BalaKJ
 */
package com.outlookgroup.outlookmags.listener;

import com.android.volley.VolleyError;


/**
 * @author BalaKJ Callback Interface used for Server API Response
 */
public interface ServerCallback<T> {
    public abstract void complete(int code);

    /**
     * @param response
     * @param apiMethod
     */
    public abstract void onAPIResponse(T response, String apiMethod);

    /**
     * @param error
     * @param apiMethod
     */
    public abstract void onErrorResponse(VolleyError error, String apiMethod);
};
