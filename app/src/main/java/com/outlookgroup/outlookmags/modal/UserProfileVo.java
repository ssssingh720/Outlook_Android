package com.outlookgroup.outlookmags.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhumita on 05-11-2015.
 */
public class UserProfileVo extends BaseVO {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("id")
    @Expose
    private String fbId;
    @SerializedName("gid")
    @Expose
    private String gId;
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user_name")
    @Expose
    private String name;
    @SerializedName("user_email")
    @Expose
    private String email;
    @SerializedName("admin")
    @Expose
    private boolean admin;
    @SerializedName("gmail_photo")
    @Expose
    private String gmailPhoto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getGmailPhoto() {
        return gmailPhoto;
    }

    public void setGmailPhoto(String gmailPhoto) {
        this.gmailPhoto = gmailPhoto;
    }
}
