package com.app.outlook.modal;

/**
 * Created by srajendrakumar on 19/10/15.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("collection")
    @Expose
    private String collection;
    @SerializedName("self")
    @Expose
    private String self;

    /**
     * @return The collection
     */
    public String getCollection() {
        return collection;
    }

    /**
     * @param collection The collection
     */
    public void setCollection(String collection) {
        this.collection = collection;
    }

    /**
     * @return The self
     */
    public String getSelf() {
        return self;
    }

    /**
     * @param self The self
     */
    public void setSelf(String self) {
        this.self = self;
    }
}
