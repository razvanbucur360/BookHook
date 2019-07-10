package com.example.bookhook2.models;

import java.io.Serializable;

public class Category implements Serializable {
    private String imageURL;
    private String name;

    public Category(){

    }

    public Category(String imageURL, String text){
        this.imageURL = imageURL;
        this.name = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }
}
