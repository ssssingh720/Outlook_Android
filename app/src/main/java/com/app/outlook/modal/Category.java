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
    private List<ListCardVo> listCardVo = new ArrayList<ListCardVo>();
    @SerializedName("cover_story")
    @Expose
    private List<ListCardVo> coverStory = new ArrayList<ListCardVo>();
    @SerializedName("national")
    @Expose
    private List<ListCardVo> national = new ArrayList<ListCardVo>();
    @SerializedName("international")
    @Expose
    private List<ListCardVo> international = new ArrayList<ListCardVo>();
    @SerializedName("business")
    @Expose
    private List<ListCardVo> business = new ArrayList<ListCardVo>();
    @SerializedName("features")
    @Expose
    private List<ListCardVo> features = new ArrayList<ListCardVo>();
    @SerializedName("regulars")
    @Expose
    private List<ListCardVo> regulars = new ArrayList<ListCardVo>();
    @SerializedName("daily")
    @Expose
    private List<ListCardVo> daily = new ArrayList<ListCardVo>();

    /**
     *
     * @return
     * The regular
     */
    public List<ListCardVo> getListCardVo() {
        return listCardVo;
    }

    /**
     *
     * @param listCardVo
     * The regular
     */
    public void setListCardVo(List<ListCardVo> listCardVo) {
        this.listCardVo = listCardVo;
    }

    /**
     *
     * @return
     * The coverStory
     */
    public List<ListCardVo> getCoverStory() {
        return coverStory;
    }

    /**
     *
     * @param coverStory
     * The cover_story
     */
    public void setCoverStory(List<ListCardVo> coverStory) {
        this.coverStory = coverStory;
    }

    /**
     *
     * @return
     * The national
     */
    public List<ListCardVo> getNational() {
        return national;
    }

    /**
     *
     * @param national
     * The national
     */
    public void setNational(List<ListCardVo> national) {
        this.national = national;
    }

    /**
     *
     * @return
     * The international
     */
    public List<ListCardVo> getInternational() {
        return international;
    }

    /**
     *
     * @param international
     * The international
     */
    public void setInternational(List<ListCardVo> international) {
        this.international = international;
    }

    /**
     *
     * @return
     * The business
     */
    public List<ListCardVo> getBusiness() {
        return business;
    }

    /**
     *
     * @param business
     * The business
     */
    public void setBusiness(List<ListCardVo> business) {
        this.business = business;
    }

    /**
     *
     * @return
     * The features
     */
    public List<ListCardVo> getFeatures() {
        return features;
    }

    /**
     *
     * @param features
     * The features
     */
    public void setFeatures(List<ListCardVo> features) {
        this.features = features;
    }

    /**
     *
     * @return
     * The regulars
     */
    public List<ListCardVo> getRegulars() {
        return regulars;
    }

    /**
     *
     * @param regulars
     * The regulars
     */
    public void setRegulars(List<ListCardVo> regulars) {
        this.regulars = regulars;
    }

    /**
     *
     * @return
     * The daily
     */
    public List<ListCardVo> getDaily() {
        return daily;
    }

    /**
     *
     * @param daily
     * The daily
     */
    public void setDaily(List<ListCardVo> daily) {
        this.daily = daily;
    }

}
