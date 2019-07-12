package com.example.bookhook2.models;

import com.google.firebase.database.Exclude;

import org.joda.time.LocalDate;

import java.io.Serializable;

public class Event implements Serializable {
    private String name;
    private String location;
    private String description;
    private LocalDate localDate;
    private String imageURL;
    private String address;
    private String stringDate;
    private String price;
    private String category;

    public Event(){

    }

    public Event(String name, String location, String description, LocalDate date, String imageURL, String address) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.localDate = date;
        this.imageURL = imageURL;
        this.address = address;
    }

    public Event(String name, String location, String description, String imageURL, String address, String stringDate, String price, String category) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.imageURL = imageURL;
        this.address = address;
        this.stringDate = stringDate;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    @Exclude
    public LocalDate getDate() {
        return localDate;
    }

    public String getStringDate(){
        return stringDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}
