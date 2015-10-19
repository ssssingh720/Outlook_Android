package com.app.outlook.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by srajendrakumar on 19/10/15.
 */
public class WeeklyIssueVo {

    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("select_issue_post")
    @Expose
    private ArrayList<Integer> selectIssuePost;

    /**
     *
     * @return
     * The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @param displayName
     * The display_name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     *
     * @return
     * The coverImage
     */
    public String getCoverImage() {
        return coverImage;
    }

    /**
     *
     * @param coverImage
     * The cover_image
     */
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    /**
     *
     * @return
     * The selectIssuePost
     */
    public ArrayList<Integer> getSelectIssuePost() {
        return selectIssuePost;
    }

    /**
     *
     * @param selectIssuePost
     * The select_issue_post
     */
    public void setSelectIssuePost(ArrayList<Integer> selectIssuePost) {
        this.selectIssuePost = selectIssuePost;
    }
}
