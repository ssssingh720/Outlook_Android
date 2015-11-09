package com.app.outlook.modal;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 03/11/15.
 */
public class Month {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("issues")
    @Expose
    private List<Issue> issues = new ArrayList<Issue>();

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The issues
     */
    public List<Issue> getIssues() {
        return issues;
    }

    /**
     * @param issues The issues
     */
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }
}
