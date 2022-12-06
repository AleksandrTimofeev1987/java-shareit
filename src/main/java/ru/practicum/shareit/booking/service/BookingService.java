package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingCreate;

public interface BookingService {

    Booking getBookingById(Long userId, Long bookingId);

    Booking createBooking(Long userId, BookingCreate booking);

    Booking setBookingStatus(Long userId, Long bookingId, Boolean approved);

}
