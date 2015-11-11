package com.app.outlook.modal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 16/10/15.
 */

public class Category implements Serializable{

    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_icon")
    @Expose
    private String categoryIcon;
    @SerializedName("category_type")
    @Expose
    private String categoryType;
    @SerializedName("cards")
    @Expose
    private List<Card> cards = new ArrayList<Card>();
    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();

    /**
     * @return The categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName The category_name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return The categoryIcon
     */
    public String getCategoryIcon() {
        return categoryIcon;
    }

    /**
     * @param categoryIcon The category_icon
     */
    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    /**
     * @return The categoryType
     */
    public String getCategoryType() {
        return categoryType;
    }

    /**
     * @param categoryType The category_type
     */
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    /**
     * @return The cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * @param cards The cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * @return The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * @param categories The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
