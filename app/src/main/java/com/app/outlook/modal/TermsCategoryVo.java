package com.app.outlook.modal;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 19/10/15.
 */
public class TermsCategoryVo {

    @SerializedName("ID")
    @Expose
    private Integer ID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("taxonomy")
    @Expose
    private String taxonomy;
    @SerializedName("parent")
    @Expose
    private Object parent;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("acf")
    @Expose
    private List<Object> acf = new ArrayList<Object>();

    /**
     *
     * @return
     * The ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     *
     * @param ID
     * The ID
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

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
     * The slug
     */
    public String getSlug() {
        return slug;
    }

    /**
     *
     * @param slug
     * The slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
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
     * The taxonomy
     */
    public String getTaxonomy() {
        return taxonomy;
    }

    /**
     *
     * @param taxonomy
     * The taxonomy
     */
    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    /**
     *
     * @return
     * The parent
     */
    public Object getParent() {
        return parent;
    }

    /**
     *
     * @param parent
     * The parent
     */
    public void setParent(Object parent) {
        this.parent = parent;
    }

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The link
     */
    public String getLink() {
        return link;
    }

    /**
     *
     * @param link
     * The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     *
     * @return
     * The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     *
     * @param meta
     * The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    /**
     *
     * @return
     * The acf
     */
    public List<Object> getAcf() {
        return acf;
    }

    /**
     *
     * @param acf
     * The acf
     */
    public void setAcf(List<Object> acf) {
        this.acf = acf;
    }

}
