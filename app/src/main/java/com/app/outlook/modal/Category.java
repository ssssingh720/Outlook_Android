package com.app.outlook.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srajendrakumar on 16/10/15.
 */

public class Category {

    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<>();

    /**
     * @return The categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName The categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return The categoryId
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId The categoryId
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }
}
