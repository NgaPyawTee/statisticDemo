package com.homework.statisticdemo.model;

public class Date {
    String amount, percent, date, currentTime, imageUrl, id;

    public Date(){

    }

    public Date(String amount, String percent, String date, String currentTime, String imageUrl) {
        if (imageUrl.trim().equals("")){
            this.imageUrl = "No photo yet";
        }else{
            this.imageUrl = imageUrl;
        }
        this.amount = amount;
        this.percent = percent;
        this.date = date;
        this.currentTime = currentTime;
    }

    public String getAmount() {
        return amount;
    }

    public String getPercent() {
        return percent;
    }

    public String getDate() {
        return date;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
