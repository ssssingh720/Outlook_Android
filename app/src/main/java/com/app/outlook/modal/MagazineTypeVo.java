package com.app.outlook.modal;

/**
 * Created by srajendrakumar on 02/11/15.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MagazineTypeVo implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("qtly")
    @Expose
    private String qtly;
    @SerializedName("haly_yearly")
    @Expose
    private String halyYearly;
    @SerializedName("annual")
    @Expose
    private String annual;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * The logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     *
     * @param logo
     * The logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     *
     * @return
     * The qtly
     */
    public String getQtly() {
        return qtly;
    }

    /**
     *
     * @param qtly
     * The qtly
     */
    public void setQtly(String qtly) {
        this.qtly = qtly;
    }

    /**
     *
     * @return
     * The halyYearly
     */
    public String getHalyYearly() {
        return halyYearly;
    }

    /**
     *
     * @param halyYearly
     * The haly_yearly
     */
    public void setHalyYearly(String halyYearly) {
        this.halyYearly = halyYearly;
    }

    /**
     *
     * @return
     * The annual
     */
    public String getAnnual() {
        return annual;
    }

    /**
     *
     * @param annual
     * The annual
     */
    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
