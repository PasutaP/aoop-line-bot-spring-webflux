package com.example.lineapibackend.utils;

public enum RoomTypes {
    DELUXE("Deluxe Room"),
    DOUBLE("Double Room"),
    FAMILY("Family Room"),
    SUPERIOR_TWIN("Superior Twin Room");


    private String type;

    RoomTypes(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
