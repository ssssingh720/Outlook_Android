package com.outlookgroup.outlookmags.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 16/11/15.
 */
public class PurchaseResponseVo extends BaseVO{

    @SerializedName("response")
    @Expose
    private Response response;

    /**
     *
     * @return
     * The response
     */
    public Response getResponse() {
        return response;
    }

    /**
     *
     * @param response
     * The response
     */
    public void setResponse(Response response) {
        this.response = response;
    }
}
