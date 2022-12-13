package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.RequestState;

import java.util.List;

public interface BookingService {

    /**
     * Method returns list of bookings made by user.
     *
     * @param userId - ID of user.
     * @param state - selection parameter, can be WAITING, REJECTED("R"), ALL("A"), CURRENT("C"), PAST("P"), FUTURE("F").
     * @return - List of bookings made by user.
     */
    List<Booking> getAllBookingsByBooker(Long userId, RequestState state);

    /**
     * Method returns list of bookings made for items owned by user.
     *
     * @param userId - ID of user.
     * @param state - selection parameter, can be WAITING, REJECTED("R"), ALL("A"), CURRENT("C"), PAST("P"), FUTURE("F").
     * @return - List of bookings made for items owned by user.
     */
    List<Booking> getAllBookingsByOwner(Long userId, RequestState state);

    /**
     * Method returns booking by ID of booker or item owner.
     *
     * @param userId - ID of user requesting information.
     * @param bookingId - ID of booking requested.
     * @return - Booking with requested ID.
     */
    Booking getBookingById(Long userId, Long bookingId);

    /**
     * Method adds booking to repository.
     *
     * @param userId - ID of user adding item.
     * @param booking - Booking to be added.
     * @return - Added booking with assigned ID.
     */
    Booking createBooking(Long userId, BookingEntity booking);

    /**
     * Method updates booking status in repository. Can be done only by item owner.
     *
     * @param userId - ID of user updating item.
     * @param bookingId - Booking to be updated.
     * @param approved - Is booking approved? true - set APPROVED status to booking, false - set REJECTED status to booking.
     * @return - Updated booking.
     */
    Booking setBookingStatus(Long userId, Long bookingId, Boolean approved);
}
