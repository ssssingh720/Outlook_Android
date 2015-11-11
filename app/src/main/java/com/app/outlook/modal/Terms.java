package com.app.outlook.modal;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 19/10/15.
 */
public class Terms {

    @SerializedName("category")
    @Expose
    private List<TermsCategoryVo> category = new ArrayList<TermsCategoryVo>();

    /**
     * @return The category
     */
    public List<TermsCategoryVo> getCategory() {
        return category;
    }

    /**
     * @param category The category
     */
    public void setCategory(List<TermsCategoryVo> category) {
        this.category = category;
    }
}
