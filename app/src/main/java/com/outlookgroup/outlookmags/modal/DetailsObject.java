package com.outlookgroup.outlookmags.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srajendrakumar on 16/10/15.
 */
public class DetailsObject extends BaseVO {

    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();

    /**
     * @return The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * @param categories The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
