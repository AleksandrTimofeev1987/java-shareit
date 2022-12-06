package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingCreate;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking getBookingById(Long userId, Long bookingId) {
        log.debug("Request to get booking with id - {} from user with id - {} is received.", bookingId, userId);
        validateUserExists(userId);
        validateBookingExists(bookingId);
        validateUserOwnItemOrUserCreatedBooking(userId, bookingId);

        BookingCreate savedBooking = bookingRepository.findByOwnerOrBooker(userId, bookingId);
        Booking result = mapBookingCreateToBooking(savedBooking);
        log.debug("Booking with ID - {} is found.", result.getId());
        return result;
    }

    @Override
    public Booking createBooking(Long userId, BookingCreate booking) {
        log.debug("Request to add booking for item with id - {} is received.", booking.getItemId());
        validateUserExists(userId);
        validateItemExists(booking.getItemId());
        validateStartBeforeEnd(booking);
        validateItemAvailable(booking.getItemId());
        validateUserIsNotItemOwner(userId, booking.getItemId());

        BookingCreate savedBooking = bookingRepository.save(booking);
        Booking result = mapBookingCreateToBooking(savedBooking);
        log.debug("Booking with ID - {} is added to repository.", result.getId());
        return result;
    }

    @Transactional
    @Override
    public Booking setBookingStatus(Long userId, Long bookingId, Boolean approved) {
        log.debug("Request to set booking status for booking with id - {} (approved = {}) is received.", bookingId, approved);
        validateUserExists(userId);
        validateBookingExists(bookingId);
        validateUserOwnItem(userId, bookingId);

        BookingCreate savedBooking = bookingRepository.findById(bookingId).get();

        if (approved) {
            validateBookingIsNotApproved(bookingId);
            savedBooking.setStatus(BookingStatus.APPROVED);
        } else {
            savedBooking.setStatus(BookingStatus.REJECTED);
        }

        BookingCreate updatedBooking = bookingRepository.save(savedBooking);


        Booking result = mapBookingCreateToBooking(updatedBooking);
        log.debug("Booking with ID - {} is {}", result.getId(), result.getStatus().toString());
        return result;
    }

    private Booking mapBookingCreateToBooking(BookingCreate savedBooking) {
        if (savedBooking == null) {
            return null;
        }

        Booking booking = new Booking();

        booking.setId(savedBooking.getId());
        booking.setItem(itemRepository.getItemShortById(savedBooking.getItemId()));
        booking.setBooker(userRepository.getUserShortById(savedBooking.getBookerId()));
        booking.setStart(savedBooking.getStart());
        booking.setEnd(savedBooking.getEnd());
        booking.setStatus(savedBooking.getStatus());

        return booking;
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id: %d is not found", userId));
        }
    }

    private void validateItemExists(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(String.format("Item with id: %d is not found", itemId));
        }
    }

    private void validateBookingExists(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException(String.format("Booking with id: %d is not found", bookingId));
        }
    }

    private void validateStartBeforeEnd(BookingCreate booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BadRequestException("Booking end should not be before booking end");
        }
    }

    private void validateItemAvailable(Long itemId) {
        if (!itemRepository.getItemAvailability(itemId)) {
            throw new BadRequestException(String.format("Item with ID - %d is not available", itemId));
        }
    }

    private void validateUserOwnItem(Long userId, Long bookingId) {
        if (!userId.equals(bookingRepository.getItemOwnerId(bookingId))) {
            throw new NotFoundException(String.format("Item in booking with ID: %d is not owned by user with ID: %d", bookingId, userId));
        }
    }

    private void validateUserIsNotItemOwner(Long userId, Long itemId) {
        if (userId.equals(itemRepository.getItemOwnerId(itemId))) {
            throw new NotFoundException("User cannot book his own item");
        }
    }

    private void validateBookingIsNotApproved(Long bookingId) {
        if (BookingStatus.APPROVED.equals(bookingRepository.getBookingStatus(bookingId))) {
            throw new BadRequestException(String.format("Booking with ID: %d is already approved", bookingId));
        }
    }

    private void validateUserOwnItemOrUserCreatedBooking(Long userId, Long bookingId) {
        if (!userId.equals(bookingRepository.getItemOwnerId(bookingId)) && !userId.equals(bookingRepository.getBookerId(bookingId))) {
            throw new NotFoundException("Request to get booking is received from user who is not item owner or booker");
        }
    }
}
