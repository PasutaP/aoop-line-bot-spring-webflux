package com.example.lineapibackend.service.Room;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.repository.BookingRepository;
import com.example.lineapibackend.repository.RoomRepository;
import com.example.lineapibackend.service.Booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;


@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final BookingService bookingService;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, BookingService bookingService) {
        this.roomRepository = roomRepository;
        this.bookingService = bookingService;
    }

//    TODO: RoomService Methods implementation

    @Override
    public Mono<Room> createRoom(Room room) {
        return roomRepository.insert(room);
    }

//    TODO: Resolve this magic
    @Override
    public Mono<Room> updateRoom(Room room, String id) {
        return findById(id)
                .doOnSuccess(roomRepository::save)
                .switchIfEmpty(Mono.error(new Exception("No room found with id: " + id)));
    }

    @Override
    public Flux<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Mono<Room> findById(String id) {
        return roomRepository.findById(id);
    }

    @Override
    public Flux<Room> findAvailableRoomByDateRange(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return roomRepository.deleteById(id)
                .flatMap(room -> Mono.just(Boolean.TRUE));
    }

    @Override
    public Mono<Room> findFirstByType(String type) {
        return roomRepository.findFirstByType(type);
    }

    @Override
    public Flux<Room> findRoomsBytype(String type) {
        return roomRepository.findRoomsByType(type);
    }

    @Override
    public Flux<Room> findRoomsByAvailabilityAndType(boolean availability, String type) {
        return roomRepository.findRoomsByAvailabilityAndType(availability, type);
    }

    @Override
    public Mono<Integer> countRoomByAvailableRoomsByType(boolean availability, String type) {
        return roomRepository.countRoomsByAvailabilityAndType(true, type);
    }

//    TODO: delete this method
    @Override
    public Flux<Room> findRoomsFromBookingByUserId(String userId) {
        return null;
    }

    @Override
    public Flux<Room> findRoomsNotInRoomList(List<Room> rooms) {
        return roomRepository.findAll()
                .filter(room -> !rooms.contains(room))
                .flatMap(Flux::just);
    }

}
