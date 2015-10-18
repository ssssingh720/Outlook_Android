package com.app.outlook.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Inkoniq-Admin on 18-Oct-15.
 */
public class Image {

    @SerializedName("ID")
    @Expose
    private Integer ID;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("alt")
    @Expose
    private String alt;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("mime_type")
    @Expose
    private String mimeType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("sizes")
    @Expose
    private Sizes sizes;

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
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename The filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The alt
     */
    public String getAlt() {
        return alt;
    }

    /**
     * @param alt The alt
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
     * @return The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption The caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

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
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return The modified
     */
    public String getModified() {
        return modified;
    }

    /**
     * @param modified The modified
     */
    public void setModified(String modified) {
        this.modified = modified;
    }

    /**
     * @return The mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType The mime_type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return The sizes
     */
    public Sizes getSizes() {
        return sizes;
    }

    /**
     * @param sizes The sizes
     */
    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }
}
