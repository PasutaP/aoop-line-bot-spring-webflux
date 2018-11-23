package com.example.lineapibackend.entity.roomDetail;

import com.example.lineapibackend.utils.RoomTypes;

public class RoomDetailFactory {
    private static RoomDetailFactory ourInstance = new RoomDetailFactory();

    public static RoomDetailFactory getInstance() {
        return ourInstance;
    }

    private RoomDetailFactory() {
    }

    public static RoomDetail getRoomDetail(String roomType) {
        if(roomType == null)return null;
        else if(RoomTypes.DELUXE.toString().equals(roomType))return new DeluxeRoomDetail();
        else if(RoomTypes.DOUBLE.toString().equals(roomType))return new DoubleRoomDetail();
        else if(RoomTypes.FAMILY.toString().equals(roomType))return new FamilyRoomDetail();
        else if(RoomTypes.SUPERIOR_TWIN.toString().equals(roomType))return new SuperiorTwinRoomDetail();
        return null;
    }
}
