package com.app.outlook.modal;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 03/11/15.
 */
public class YearListVo {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("months")
    @Expose
    private List<Month> months = new ArrayList<Month>();

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
     * The year
     */
    public String getYear() {
        return year;
    }

    /**
     *
     * @param year
     * The year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     *
     * @return
     * The months
     */
    public List<Month> getMonths() {
        return months;
    }

    /**
     *
     * @param months
     * The months
     */
    public void setMonths(List<Month> months) {
        this.months = months;
    }
}
