package com.example.lineapibackend.entity.roomDetail;

import com.example.lineapibackend.entity.roomDetail.RoomDetail;
import com.example.lineapibackend.utils.RoomTypes;

public class DeluxeRoomDetail implements RoomDetail {

    @Override
    public String getRoomType() {
        return RoomTypes.DELUXE.toString();
    }

    @Override
    public int getSleeps() {
        return 2;
    }

    @Override
    public double getPrice() {
        return 2050;
    }

    @Override
    public String getRoomImageUrl() {
        return "https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/RoomType%2FDeluxeRoom.jpg?alt=media&token=dc48ff4a-0f49-49ab-b328-6a962b81c117";

    }
}
