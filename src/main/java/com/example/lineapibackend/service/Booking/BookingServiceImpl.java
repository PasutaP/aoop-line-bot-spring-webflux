package com.example.lineapibackend.service.Booking;

import com.example.lineapibackend.entity.Booking;
import com.example.lineapibackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Mono<Booking> createBooking(Booking booking) {
        return bookingRepository.insert(booking);
    }

//    TODO: Resolve this magic
    @Override
    public Mono<Booking> updateBooking(Booking booking, String id) {
        return findById(id)
                .doOnSuccess(bookingRepository::save)
                .switchIfEmpty(Mono.error(new Exception("No booking found with id: " + id)));
    }

    @Override
    public Flux<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Mono<Booking> findById(String id) {
        return bookingRepository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("No booking found with id: " + id) ));
    }

    @Override
    public Flux<Booking> findByUserId(String userId) {
        return bookingRepository.findBookingsByBookedByUserId(userId);
    }

    @Override
    public Flux<Booking> findByRange(Date startDate, Date endDate) {
        return findAll()
                .filter(booking ->
                    booking.getCheckInDate().after(startDate) &&
                            booking.getCheckOutDate().before(endDate));
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return bookingRepository.deleteById(id)
                .flatMap(booking -> Mono.just(Boolean.TRUE))
                .doOnError(booking -> Mono.just(Boolean.FALSE));
    }


}
