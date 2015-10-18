package com.app.outlook.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srajendrakumar on 16/10/15.
 */
public class ListCardVo {
    @SerializedName("item")
    @Expose
    private List<Item> item = new ArrayList<Item>();

    /**
     * @return The item
     */
    public List<Item> getItem() {
        return item;
    }

    /**
     * @param item The item
     */
    public void setItem(List<Item> item) {
        this.item = item;
    }
}
