package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.RequestStatus;

import java.util.List;

public interface BookingService {

    List<Booking> getAllBookingsByBooker(Long userId, RequestStatus requestStatus);

    List<Booking> getAllBookingsByOwner(Long userId, RequestStatus state);

    Booking getBookingById(Long userId, Long bookingId);

    Booking createBooking(Long userId, BookingEntity booking);

    Booking setBookingStatus(Long userId, Long bookingId, Boolean approved);
}
