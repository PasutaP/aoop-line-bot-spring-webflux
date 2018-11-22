package com.example.lineapibackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document(collection = "Booking")
public class Booking {

    @Id
    public String id;
    public String bookedByUserId;
    public Date createdDate;
    public Date checkInDate;
    public Date checkOutDate;
    public double totalPrice;
    public List<String> roomList;

    public Booking() {
        this.createdDate = Calendar.getInstance().getTime();
        this.roomList = new ArrayList<>();
    }

    public String getBookedByUserId() {
        return bookedByUserId;
    }

    public void setBookedByUserId(String bookedByUserId) {
        this.bookedByUserId = bookedByUserId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<String> roomList) {
        this.roomList = roomList;
    }
}
