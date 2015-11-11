package com.app.outlook.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 19/10/15.
 */
public class IssuesVo {

    @SerializedName("ID")
    @Expose
    private Integer ID;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("parent")
    @Expose
    private Object parent;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("excerpt")
    @Expose
    private Object excerpt;
    @SerializedName("terms")
    @Expose
    private Terms terms;

    /**
     * @return The ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @param ID The ID
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return The link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return The format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format The format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return The parent
     */
    public Object getParent() {
        return parent;
    }

    /**
     * @param parent The parent
     */
    public void setParent(Object parent) {
        this.parent = parent;
    }

    /**
     * @return The slug
     */
    public String getSlug() {
        return slug;
    }

    /**
     * @param slug The slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * @return The excerpt
     */
    public Object getExcerpt() {
        return excerpt;
    }

    /**
     * @param excerpt The excerpt
     */
    public void setExcerpt(Object excerpt) {
        this.excerpt = excerpt;
    }

    /**
     * @return The terms
     */
    public Terms getTerms() {
        return terms;
    }

    /**
     * @param terms The terms
     */
    public void setTerms(Terms terms) {
        this.terms = terms;
    }

}
