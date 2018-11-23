package com.example.lineapibackend.entity.roomDetail;

import com.example.lineapibackend.utils.RoomTypes;

public class FamilyRoomDetail implements RoomDetail {
    @Override
    public String getRoomType() {
        return RoomTypes.FAMILY.toString();
    }

    @Override
    public int getSleeps() {
        return 4;
    }

    @Override
    public double getPrice() {
        return 3150;
    }

    @Override
    public String getRoomImageUrl() {
        return "https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/RoomType%2FFamilyRoom.jpg?alt=media&token=e21794db-a11f-4047-9627-226b71429ce1";
    }
}
