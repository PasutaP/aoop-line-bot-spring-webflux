package com.example.lineapibackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "User")
public class User {
    public String name;
    public String pictureUrl;

    @Id
    public String userId;
    public List<String> bookingList;
    public boolean checkInStatus;

    public User() {
        this.bookingList = new ArrayList<>();
        this.checkInStatus = false;
    }

    public User(String name, String pictureUrl, String userId) {
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.userId = userId;
        this.bookingList = new ArrayList<>();
        this.checkInStatus = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<String> bookingList) {
        this.bookingList = bookingList;
    }

    public boolean isCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(boolean checkinStatus) {
        this.checkInStatus = checkinStatus;
    }
}
