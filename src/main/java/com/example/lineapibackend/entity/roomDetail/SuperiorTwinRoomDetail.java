package com.example.lineapibackend.entity.roomDetail;

import com.example.lineapibackend.utils.RoomTypes;

public class SuperiorTwinRoomDetail implements RoomDetail {
    @Override
    public String getRoomType() {
        return RoomTypes.SUPERIOR_TWIN.toString();
    }

    @Override
    public int getSleeps() {
        return 2;
    }

    @Override
    public double getPrice() {
        return 1603;
    }

    @Override
    public String getRoomImageUrl() {
        return "https://firebasestorage.googleapis.com/v0/b/aoop-project-d1add.appspot.com/o/RoomType%2FSuperiorTwin.jpg?alt=media&token=1fda5386-4477-49f1-9b30-5ede19c68bc8";
    }
}
