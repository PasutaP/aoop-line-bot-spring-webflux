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
    public Room bookedRoom;

    public Booking() {
        this.createdDate = Calendar.getInstance().getTime();
    }

    public Booking(Date checkInDate, Date checkOutDate, Room bookedRoom, String userId) {
        this();
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = bookedRoom.getPrice();
        this.bookedByUserId = userId;
        this.bookedRoom = bookedRoom;
    }

    public String getBookedByUserId() {
        return bookedByUserId;
    }

    public void setBookedByUserId(String bookedByUserId) {
        this.bookedByUserId = bookedByUserId;
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

    public Room getBookedRoom() {
        return bookedRoom;
    }

    public void setBookedRoom(Room bookedRoom) {
        this.bookedRoom = bookedRoom;
    }
}
