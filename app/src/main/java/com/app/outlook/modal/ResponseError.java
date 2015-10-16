package com.app.outlook.modal;

import com.android.volley.VolleyError;

/**
 * Created by BalaKJ on 2/5/15.
 */
public class ResponseError extends VolleyError {

    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
