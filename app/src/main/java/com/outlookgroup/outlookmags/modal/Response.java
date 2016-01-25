package com.outlookgroup.outlookmags.modal;

/**
 * Created by srajendrakumar on 16/11/15.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("consumptionState")
    @Expose
    private Integer consumptionState;
    @SerializedName("developerPayload")
    @Expose
    private String developerPayload;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("purchaseState")
    @Expose
    private Integer purchaseState;
    @SerializedName("purchaseTimeMillis")
    @Expose
    private String purchaseTimeMillis;

    /**
     * @return The consumptionState
     */
    public Integer getConsumptionState() {
        return consumptionState;
    }

    /**
     * @param consumptionState The consumptionState
     */
    public void setConsumptionState(Integer consumptionState) {
        this.consumptionState = consumptionState;
    }

    /**
     * @return The developerPayload
     */
    public String getDeveloperPayload() {
        return developerPayload;
    }

    /**
     * @param developerPayload The developerPayload
     */
    public void setDeveloperPayload(String developerPayload) {
        this.developerPayload = developerPayload;
    }

    /**
     * @return The kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind The kind
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return The purchaseState
     */
    public Integer getPurchaseState() {
        return purchaseState;
    }

    /**
     * @param purchaseState The purchaseState
     */
    public void setPurchaseState(Integer purchaseState) {
        this.purchaseState = purchaseState;
    }

    /**
     * @return The purchaseTimeMillis
     */
    public String getPurchaseTimeMillis() {
        return purchaseTimeMillis;
    }

    /**
     * @param purchaseTimeMillis The purchaseTimeMillis
     */
    public void setPurchaseTimeMillis(String purchaseTimeMillis) {
        this.purchaseTimeMillis = purchaseTimeMillis;
    }
}