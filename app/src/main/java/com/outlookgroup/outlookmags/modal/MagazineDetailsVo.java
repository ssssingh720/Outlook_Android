package com.outlookgroup.outlookmags.modal;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 03/11/15.
 */
public class MagazineDetailsVo extends BaseVO {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("issue_id")
    @Expose
    private String issueId;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();

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
     * @return The issueId
     */
    public String getIssueId() {
        return issueId;
    }

    /**
     * @param issueId The issue_id
     */
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    /**
     * @return The year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year The year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return The month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month The month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * @return The issue
     */
    public String getIssue() {
        return issue;
    }

    /**
     * @param issue The issue
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

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
