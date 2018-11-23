package com.example.lineapibackend.entity.roomDetail;

import com.example.lineapibackend.utils.RoomTypes;

public class DoubleRoomDetail implements RoomDetail {
    @Override
    public String getRoomType() {
        return RoomTypes.DOUBLE.toString();
    }

    @Override
    public int getSleeps() {
        return 2;
    }

    @Override
    public double getPrice() {
        return 1978;
    }

    @Override
    public String getRoomImageUrl() {
        return "https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/RoomType%2FDoubleRoom.jpg?alt=media&token=df6a91a4-69bf-4baf-a809-1d3d164741ce";
    }
}
