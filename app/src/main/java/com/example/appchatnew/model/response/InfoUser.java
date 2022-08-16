package com.example.appchatnew.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InfoUser implements Serializable {
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("privateKeyFirst")
    @Expose
    private String privateKeyFirst;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("username")
    @Expose
    private String username;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKeyFirst() {
        return privateKeyFirst;
    }

    public void setPrivateKeyFirst(String privateKeyFirst) {
        this.privateKeyFirst = privateKeyFirst;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
