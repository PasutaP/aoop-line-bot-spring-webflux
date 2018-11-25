package com.example.lineapibackend.controller;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.entity.Room;
import com.example.lineapibackend.service.Booking.BookingService;
import com.example.lineapibackend.service.Room.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/booking")
public class BookingController {
    private static final String TAG = "BookingController";
    private Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    private final BookingService bookingService;

    @Autowired
    public BookingController(RoomService roomService, BookingService bookingService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Booking> findAll() {
        logger.debug(TAG + ": Find all bookings");
        return bookingService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<Booking> findById(@PathVariable String id) {
        logger.debug(TAG + ": Find booking by Id - " + id);
        return bookingService.findById(id);
    }

    @GetMapping(value = "/", params = {"userId"})
    public Flux<Booking> findByUserId(@RequestParam String userId) {
        logger.debug(TAG + ": Find booking by userId - " + userId);
        return bookingService.findByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Booking> createBooking(@RequestBody Booking booking) {
        logger.debug(TAG + ": Create a Booking");
        return bookingService.createBooking(booking);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Boolean> delete(@PathVariable String id) {
        logger.debug(TAG + ": Delete booking with id - " + id);
        return bookingService.delete(id);
    }

    @PutMapping(value = "/{id}")
    public Mono<Booking> updateBooking(@RequestBody Booking booking, @PathVariable String id) {
        return bookingService.updateBooking(booking, id);
    }

    @PostMapping(value = "/mock")
    public Mono<Booking> mockBooking() {
        logger.debug(TAG + ": Mock Booking");

        return roomService.findFirstByType("Double Room")
                .flatMap(room -> {
                    Booking newBooking = new Booking();
                    newBooking.setBookedByUserId("Uaa2206bc051835dedc525d86e592908b");
                    newBooking.setBookedRoom(room);
                    newBooking.setCheckInDate(new GregorianCalendar().getTime());
                    newBooking.setCheckOutDate(new GregorianCalendar(2019, GregorianCalendar.JULY,1).getTime());
                    logger.debug(TAG + ": Booking created - " + newBooking);
                    return Mono.just(newBooking);
                }).flatMap(bookingService::createBooking);

    }

}
