package com.example.appchatnew.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUser {
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("privateKeyFirst")
    @Expose
    private String privateKeyFirst;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public RequestUser(String fullname, String username, String password, String telephone, String privateKeyFirst, String avatar) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.telephone = telephone;
        this.privateKeyFirst = privateKeyFirst;
        this.avatar = avatar;
    }

    public RequestUser() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPrivateKeyFirst() {
        return privateKeyFirst;
    }

    public void setPrivateKeyFirst(String privateKeyFirst) {
        this.privateKeyFirst = privateKeyFirst;
    }
}
