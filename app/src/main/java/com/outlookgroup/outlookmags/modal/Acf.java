package com.outlookgroup.outlookmags.modal;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srajendrakumar on 19/10/15.
 */
public class Acf {

    @SerializedName("january")
    @Expose
    private List<WeeklyIssueVo> january = new ArrayList<WeeklyIssueVo>();
    @SerializedName("february")
    @Expose
    private List<WeeklyIssueVo> february = new ArrayList<WeeklyIssueVo>();
    @SerializedName("march")
    @Expose
    private List<WeeklyIssueVo> march = new ArrayList<WeeklyIssueVo>();
    @SerializedName("april")
    @Expose
    private List<WeeklyIssueVo> april = new ArrayList<WeeklyIssueVo>();
    @SerializedName("may")
    @Expose
    private List<WeeklyIssueVo> may = new ArrayList<WeeklyIssueVo>();
    @SerializedName("june")
    @Expose
    private List<WeeklyIssueVo> june = new ArrayList<WeeklyIssueVo>();
    @SerializedName("july")
    @Expose
    private List<WeeklyIssueVo> july = new ArrayList<WeeklyIssueVo>();
    @SerializedName("august")
    @Expose
    private List<WeeklyIssueVo> august = new ArrayList<WeeklyIssueVo>();
    @SerializedName("september")
    @Expose
    private List<WeeklyIssueVo> september = new ArrayList<WeeklyIssueVo>();
    @SerializedName("october")
    @Expose
    private List<WeeklyIssueVo> october = new ArrayList<WeeklyIssueVo>();
    @SerializedName("november")
    @Expose
    private List<WeeklyIssueVo> november = new ArrayList<WeeklyIssueVo>();
    @SerializedName("december")
    @Expose
    private List<WeeklyIssueVo> december = new ArrayList<WeeklyIssueVo>();

    /**
     * @return The january
     */
    public List<WeeklyIssueVo> getJanuary() {
        return january;
    }

    /**
     * @param january The january
     */
    public void setJanuary(List<WeeklyIssueVo> january) {
        this.january = january;
    }

    /**
     * @return The february
     */
    public List<WeeklyIssueVo> getFebruary() {
        return february;
    }

    /**
     * @param february The february
     */
    public void setFebruary(List<WeeklyIssueVo> february) {
        this.february = february;
    }

    /**
     * @return The march
     */
    public List<WeeklyIssueVo> getMarch() {
        return march;
    }

    /**
     * @param march The march
     */
    public void setMarch(List<WeeklyIssueVo> march) {
        this.march = march;
    }

    /**
     * @return The april
     */
    public List<WeeklyIssueVo> getApril() {
        return april;
    }

    /**
     * @param april The april
     */
    public void setApril(List<WeeklyIssueVo> april) {
        this.april = april;
    }

    /**
     * @return The may
     */
    public List<WeeklyIssueVo> getMay() {
        return may;
    }

    /**
     * @param may The may
     */
    public void setMay(List<WeeklyIssueVo> may) {
        this.may = may;
    }

    /**
     * @return The june
     */
    public List<WeeklyIssueVo> getJune() {
        return june;
    }

    /**
     * @param june The june
     */
    public void setJune(List<WeeklyIssueVo> june) {
        this.june = june;
    }

    /**
     * @return The july
     */
    public List<WeeklyIssueVo> getJuly() {
        return july;
    }

    /**
     * @param july The july
     */
    public void setJuly(List<WeeklyIssueVo> july) {
        this.july = july;
    }

    /**
     * @return The august
     */
    public List<WeeklyIssueVo> getAugust() {
        return august;
    }

    /**
     * @param august The august
     */
    public void setAugust(List<WeeklyIssueVo> august) {
        this.august = august;
    }

    /**
     * @return The september
     */
    public List<WeeklyIssueVo> getSeptember() {
        return september;
    }

    /**
     * @param september The september
     */
    public void setSeptember(List<WeeklyIssueVo> september) {
        this.september = september;
    }

    /**
     * @return The october
     */
    public List<WeeklyIssueVo> getOctober() {
        return october;
    }

    /**
     * @param october The october
     */
    public void setOctober(List<WeeklyIssueVo> october) {
        this.october = october;
    }

    /**
     * @return The november
     */
    public List<WeeklyIssueVo> getNovember() {
        return november;
    }

    /**
     * @param november The november
     */
    public void setNovember(List<WeeklyIssueVo> november) {
        this.november = november;
    }

    /**
     * @return The december
     */
    public List<WeeklyIssueVo> getDecember() {
        return december;
    }

    /**
     * @param december The december
     */
    public void setDecember(List<WeeklyIssueVo> december) {
        this.december = december;
    }

}
