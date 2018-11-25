package com.example.lineapibackend.service.Booking;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface BookingService {
    Mono<Booking> createBooking(Booking booking);

    Mono<Booking> updateBooking(Booking booking, String id);

    Flux<Booking> findAll();

    Mono<Booking> findById(String id);

    Flux<Booking> findByRange(Date startDate, Date endDate);

    Mono<Boolean> delete(String id);

    Flux<Booking> findByUserId(String userId);

//    Flux<Booking> findBy
}
