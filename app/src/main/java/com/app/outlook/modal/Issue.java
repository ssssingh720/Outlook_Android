package com.app.outlook.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 03/11/15.
 */
public class Issue {

    @SerializedName("issue_id")
    @Expose
    private Integer issueId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("purchase")
    @Expose
    private Boolean purchase;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("issue_published_date")
    @Expose
    private String issue_published_date;

    /**
     * @return The issueId
     */
    public Integer getIssueId() {
        return issueId;
    }

    /**
     * @param issueId The issue_id
     */
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    /**
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The purchase
     */
    public Boolean getPurchase() {
        return purchase;
    }

    /**
     * @param purchase The purchase
     */
    public void setPurchase(Boolean purchase) {
        this.purchase = purchase;
    }

    /**
     * @return The sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku The sku
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getIssue_published_date() {
        return issue_published_date;
    }

    public void setIssue_published_date(String issue_published_date) {
        this.issue_published_date = issue_published_date;
    }
}
