package com.app.outlook.modal;

import java.io.Serializable;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class Magazine implements Serializable {

    private String name;
    private String desciption;
    private String image;
    private int id;
    private String issueDate;
    private String postId;
    private boolean isPurchased;
    private String sku;
    private String issue_published_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public String getSku() {
        return sku;
    }

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
