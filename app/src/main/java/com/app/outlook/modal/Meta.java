package com.app.outlook.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 19/10/15.
 */
public class Meta {

    @SerializedName("links")
    @Expose
    private Links links;

    /**
     *
     * @return
     * The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     *
     * @param links
     * The links
     */
    public void setLinks(Links links) {
        this.links = links;
    }
}
