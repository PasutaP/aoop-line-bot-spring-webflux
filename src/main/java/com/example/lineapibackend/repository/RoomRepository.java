package com.example.lineapibackend.repository;

import com.example.lineapibackend.entity.Room;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RoomRepository  extends ReactiveMongoRepository<Room, String> {
    Mono<Room> findFirstByType(String type);
    Flux<Room> findRoomsByType(String type);
    Flux<Room> findRoomsByAvailabilityAndType(boolean availability, String type);
    Mono<Integer> countRoomsByAvailabilityAndType(boolean availability, String type);
}
