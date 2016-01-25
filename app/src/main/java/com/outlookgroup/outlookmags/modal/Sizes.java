package com.outlookgroup.outlookmags.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 18-Oct-15.
 */
public class Sizes {

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("thumbnail-width")
    @Expose
    private Integer thumbnailWidth;
    @SerializedName("thumbnail-height")
    @Expose
    private Integer thumbnailHeight;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("medium-width")
    @Expose
    private Integer mediumWidth;
    @SerializedName("medium-height")
    @Expose
    private Integer mediumHeight;
    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("large-width")
    @Expose
    private Integer largeWidth;
    @SerializedName("large-height")
    @Expose
    private Integer largeHeight;
    @SerializedName("post-thumbnail")
    @Expose
    private String postThumbnail;
    @SerializedName("post-thumbnail-width")
    @Expose
    private Integer postThumbnailWidth;
    @SerializedName("post-thumbnail-height")
    @Expose
    private Integer postThumbnailHeight;

    /**
     * @return The thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail The thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return The thumbnailWidth
     */
    public Integer getThumbnailWidth() {
        return thumbnailWidth;
    }

    /**
     * @param thumbnailWidth The thumbnail-width
     */
    public void setThumbnailWidth(Integer thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    /**
     * @return The thumbnailHeight
     */
    public Integer getThumbnailHeight() {
        return thumbnailHeight;
    }

    /**
     * @param thumbnailHeight The thumbnail-height
     */
    public void setThumbnailHeight(Integer thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    /**
     * @return The medium
     */
    public String getMedium() {
        return medium;
    }

    /**
     * @param medium The medium
     */
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     * @return The mediumWidth
     */
    public Integer getMediumWidth() {
        return mediumWidth;
    }

    /**
     * @param mediumWidth The medium-width
     */
    public void setMediumWidth(Integer mediumWidth) {
        this.mediumWidth = mediumWidth;
    }

    /**
     * @return The mediumHeight
     */
    public Integer getMediumHeight() {
        return mediumHeight;
    }

    /**
     * @param mediumHeight The medium-height
     */
    public void setMediumHeight(Integer mediumHeight) {
        this.mediumHeight = mediumHeight;
    }

    /**
     * @return The large
     */
    public String getLarge() {
        return large;
    }

    /**
     * @param large The large
     */
    public void setLarge(String large) {
        this.large = large;
    }

    /**
     * @return The largeWidth
     */
    public Integer getLargeWidth() {
        return largeWidth;
    }

    /**
     * @param largeWidth The large-width
     */
    public void setLargeWidth(Integer largeWidth) {
        this.largeWidth = largeWidth;
    }

    /**
     * @return The largeHeight
     */
    public Integer getLargeHeight() {
        return largeHeight;
    }

    /**
     * @param largeHeight The large-height
     */
    public void setLargeHeight(Integer largeHeight) {
        this.largeHeight = largeHeight;
    }

    /**
     * @return The postThumbnail
     */
    public String getPostThumbnail() {
        return postThumbnail;
    }

    /**
     * @param postThumbnail The post-thumbnail
     */
    public void setPostThumbnail(String postThumbnail) {
        this.postThumbnail = postThumbnail;
    }

    /**
     * @return The postThumbnailWidth
     */
    public Integer getPostThumbnailWidth() {
        return postThumbnailWidth;
    }

    /**
     * @param postThumbnailWidth The post-thumbnail-width
     */
    public void setPostThumbnailWidth(Integer postThumbnailWidth) {
        this.postThumbnailWidth = postThumbnailWidth;
    }

    /**
     * @return The postThumbnailHeight
     */
    public Integer getPostThumbnailHeight() {
        return postThumbnailHeight;
    }

    /**
     * @param postThumbnailHeight The post-thumbnail-height
     */
    public void setPostThumbnailHeight(Integer postThumbnailHeight) {
        this.postThumbnailHeight = postThumbnailHeight;
    }
}
