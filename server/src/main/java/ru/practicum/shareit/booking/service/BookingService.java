package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.RequestState;

import java.util.List;

public interface BookingService {

    /**
     * Method returns list of bookings made by user.
     *
     * @param userId ID of user.
     * @param state selection parameter, can be WAITING, REJECTED("R"), ALL("A"), CURRENT("C"), PAST("P"), FUTURE("F").
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return - List of bookings made by user.
     */
    List<BookingResponseDto> getAllBookingsByBooker(Long userId, RequestState state, Integer from, Integer size);

    /**
     * Method returns list of bookings made for items owned by user.
     *
     * @param userId ID of user.
     * @param state Selection parameter, can be WAITING, REJECTED("R"), ALL("A"), CURRENT("C"), PAST("P"), FUTURE("F").
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return - List of bookings made for items owned by user.
     */
    List<BookingResponseDto> getAllBookingsByOwner(Long userId, RequestState state, Integer from, Integer size);

    /**
     * Method returns booking by ID of booker or item owner.
     *
     * @param userId ID of user requesting information.
     * @param bookingId ID of booking requested.
     *
     * @return Booking with requested ID.
     */
    BookingResponseDto getBookingById(Long userId, Long bookingId);

    /**
     * Method adds booking to repository.
     *
     * @param userId ID of user adding item.
     * @param booking Booking to be added.
     *
     * @return Added booking with assigned ID.
     */
    BookingResponseDto createBooking(Long userId, Booking booking);

    /**
     * Method updates booking status in repository. Can be done only by item owner.
     *
     * @param userId ID of user updating item.
     * @param bookingId Booking to be updated.
     * @param approved Is booking approved? true - set APPROVED status to booking, false - set REJECTED status to booking.
     *
     * @return Updated booking.
     */
    BookingResponseDto setBookingStatus(Long userId, Long bookingId, Boolean approved);
}
