package com.app.outlook.modal;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 16/10/15.
 */

public class Category {

    @SerializedName("regular")
    @Expose
    private List<Regular> regular = new ArrayList<Regular>();
    @SerializedName("cover_story")
    @Expose
    private List<Object> coverStory = new ArrayList<Object>();
    @SerializedName("national")
    @Expose
    private List<Object> national = new ArrayList<Object>();
    @SerializedName("international")
    @Expose
    private List<Object> international = new ArrayList<Object>();
    @SerializedName("business")
    @Expose
    private List<Object> business = new ArrayList<Object>();
    @SerializedName("features")
    @Expose
    private List<Object> features = new ArrayList<Object>();
    @SerializedName("regulars")
    @Expose
    private List<Object> regulars = new ArrayList<Object>();
    @SerializedName("daily")
    @Expose
    private List<Object> daily = new ArrayList<Object>();

    /**
     *
     * @return
     * The regular
     */
    public List<Regular> getRegular() {
        return regular;
    }

    /**
     *
     * @param regular
     * The regular
     */
    public void setRegular(List<Regular> regular) {
        this.regular = regular;
    }

    /**
     *
     * @return
     * The coverStory
     */
    public List<Object> getCoverStory() {
        return coverStory;
    }

    /**
     *
     * @param coverStory
     * The cover_story
     */
    public void setCoverStory(List<Object> coverStory) {
        this.coverStory = coverStory;
    }

    /**
     *
     * @return
     * The national
     */
    public List<Object> getNational() {
        return national;
    }

    /**
     *
     * @param national
     * The national
     */
    public void setNational(List<Object> national) {
        this.national = national;
    }

    /**
     *
     * @return
     * The international
     */
    public List<Object> getInternational() {
        return international;
    }

    /**
     *
     * @param international
     * The international
     */
    public void setInternational(List<Object> international) {
        this.international = international;
    }

    /**
     *
     * @return
     * The business
     */
    public List<Object> getBusiness() {
        return business;
    }

    /**
     *
     * @param business
     * The business
     */
    public void setBusiness(List<Object> business) {
        this.business = business;
    }

    /**
     *
     * @return
     * The features
     */
    public List<Object> getFeatures() {
        return features;
    }

    /**
     *
     * @param features
     * The features
     */
    public void setFeatures(List<Object> features) {
        this.features = features;
    }

    /**
     *
     * @return
     * The regulars
     */
    public List<Object> getRegulars() {
        return regulars;
    }

    /**
     *
     * @param regulars
     * The regulars
     */
    public void setRegulars(List<Object> regulars) {
        this.regulars = regulars;
    }

    /**
     *
     * @return
     * The daily
     */
    public List<Object> getDaily() {
        return daily;
    }

    /**
     *
     * @param daily
     * The daily
     */
    public void setDaily(List<Object> daily) {
        this.daily = daily;
    }

}
