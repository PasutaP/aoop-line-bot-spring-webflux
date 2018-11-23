package com.example.lineapibackend.service.Room;

import com.example.lineapibackend.entity.Room;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface RoomService {
    Mono<Room> createRoom(Room room);

    Mono<Room> updateRoom(Room room, String id);

    Flux<Room> findAll();

    Mono<Room> findById(String id);

    Flux<Room> findAvailableRoomByDateRange(Date startDate, Date endDate);

    Mono<Boolean> delete(String id);

    Mono<Room> findFirstByType(String type);

    Flux<Room> findRoomsBytype(String type);

    Flux<Room> findRoomsByAvailabilityAndType(boolean availability, String type);

    Mono<Integer> countRoomByAvailableRoomsByType(boolean availability, String type);

    Flux<Room> findRoomsFromBookingByUserId(String bookingId);
}
