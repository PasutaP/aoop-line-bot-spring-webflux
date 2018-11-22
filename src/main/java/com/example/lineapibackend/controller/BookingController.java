package com.example.lineapibackend.controller;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.service.Booking.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/booking")
public class BookingController {
    private static final String TAG = "BookingController";
    private Logger logger = LoggerFactory.getLogger(RoomController.class);


    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
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
        Booking newBooking = new Booking();
        newBooking.setBookedByUserId("Uaa2206bc051835dedc525d86e592908b");
        newBooking.setCheckInDate(new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        newBooking.setCheckOutDate(new GregorianCalendar(2018, Calendar.FEBRUARY, 15).getTime());
        logger.debug(TAG + ": Booking created - " + newBooking);
        return bookingService.createBooking(newBooking);
    }

}
