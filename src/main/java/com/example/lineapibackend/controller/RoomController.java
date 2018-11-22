package com.example.lineapibackend.controller;

import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.service.Room.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retrofit2.http.Path;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/room")
public class RoomController {

    private static final String TAG = "RoomController";
    private Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Room> findAll() {
        logger.debug(TAG + ": get all rooms");
        return roomService.findAll();
    }

    @GetMapping(value = "/type/{type}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Room> findRoomByType(@PathVariable String type) {
        return roomService.findRoomsBytype(type);
    }

    @GetMapping(value = "/type/{type}/count")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Long> countRoomsByType(@PathVariable String type) {
        return roomService.findRoomsBytype(type)
                .count();
    }

    @GetMapping(value = "/id/{id}")
    public Mono<Room> findById(@PathVariable String id) {
        logger.debug(TAG + ": Find room by Id - " + id);
        return roomService.findById(id);
    }

    @PostMapping(value = "/id")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Room> createRoom(@RequestBody Room room) {
        logger.debug(TAG + ": Create a Room");
        return roomService.createRoom(room);
    }

    @DeleteMapping(value = "/id/{id}")
    public Mono<Boolean> delete(@PathVariable String id) {
        logger.debug(TAG + ": Delete room with Id - " + id);
        return roomService.delete(id);
    }

    @PutMapping(value = "/{id}")
    public Mono<Room> updateRoom(@RequestBody Room room, @PathVariable String id) {
        return roomService.updateRoom(room, id);
    }

}
