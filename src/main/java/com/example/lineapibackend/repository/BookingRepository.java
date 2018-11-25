package com.example.lineapibackend.repository;

import com.example.lineapibackend.entity.Booking;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Date;

@Repository
public interface BookingRepository extends ReactiveMongoRepository<Booking, String> {
    Flux<Booking> findBookingsByBookedByUserId(String userId);
}
