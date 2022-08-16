package com.example.appchatnew.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatResponse {
    @SerializedName("CUSTOMER")
    @Expose
    private String customer;
    @SerializedName("DATACOMBO")
    @Expose
    private String datacombo;
    @SerializedName("FILETHINGS")
    @Expose
    private String filethings;
    @SerializedName("GPS")
    @Expose
    private String gps;
    @SerializedName("TIME")
    @Expose
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @SerializedName("USER")
    @Expose
    private String user;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDatacombo() {
        return datacombo;
    }

    public void setDatacombo(String datacombo) {
        this.datacombo = datacombo;
    }

    public String getFilethings() {
        return filethings;
    }

    public void setFilethings(String filethings) {
        this.filethings = filethings;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
