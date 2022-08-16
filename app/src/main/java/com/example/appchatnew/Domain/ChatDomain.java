package com.example.appchatnew.Domain;

import java.io.Serializable;

public class ChatDomain implements Serializable {
    private String title;
    private String pic;
    private String description;
    private Double fee;


    public ChatDomain(String title, String pic, String description, Double fee) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }


    public String getPic() {
        return pic;
    }


    public Double getFee() {
        return fee;
    }

}
