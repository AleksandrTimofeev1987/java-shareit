package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.booking.entity.RequestState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public List<Booking> getAllBookingsByBooker(Long userId, RequestState state) {
        log.debug("Request to get all bookings made by user with id - {} is received (state = {}).", userId, state);
        validateUserExists(userId);

        List<Booking> foundBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                foundBookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                foundBookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, now, now);
                break;
            case PAST:
                foundBookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, now);
                break;
            case FUTURE:
                foundBookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, now);
                break;
            case WAITING:
                foundBookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                foundBookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
        }

        log.debug("Found all bookings made by user with id - {} in amount of {}.", userId, foundBookings.size());
        return foundBookings;
    }

    @Override
    public List<Booking> getAllBookingsByOwner(Long userId, RequestState state) {
        log.debug("Request to get all bookings for items owned by user with id - {} is received (state = {}).", userId, state);
        validateUserExists(userId);
        validateUserOwnItems(userId);

        List<Booking> foundBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                foundBookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                foundBookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, now, now);
                break;
            case PAST:
                foundBookings = bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId, now);
                break;
            case FUTURE:
                foundBookings = bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId, now);
                break;
            case WAITING:
                foundBookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                foundBookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
        }

        log.debug("Found all bookings for items owned by user with id - {} in amount of {}.", userId, foundBookings.size());
        return foundBookings;
    }

    @Override
    public Booking getBookingById(Long userId, Long bookingId) {
        log.debug("Request to get booking with id - {} from user with id - {} is received.", bookingId, userId);
        validateUserExists(userId);
        validateUserOwnItemOrUserCreatedBooking(userId, bookingId);

        Booking savedBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Booking with id: %d is not found", bookingId)));

        log.debug("Booking with ID - {} is found.", savedBooking.getId());
        return savedBooking;
    }

    @Override
    public Booking createBooking(Long userId, Booking booking) {
        log.debug("Request to add booking for item with id - {} is received.", booking.getItem().getId());
        validateStartBeforeEnd(booking);

        Item bookedItem = itemRepository.findById(booking.getItem().getId()).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", booking.getItem().getId())));
        validateItemIsAvailable(bookedItem.getId());
        validateUserIsNotItemOwner(userId, bookedItem.getOwner().getId());

        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId)));

        booking.setItem(bookedItem);
        booking.setBooker(booker);

        Booking savedBooking = bookingRepository.save(booking);

        log.debug("Booking with ID - {} is added to repository.", savedBooking.getId());
        return savedBooking;
    }


    @Override
    public Booking setBookingStatus(Long userId, Long bookingId, Boolean approved) {
        log.debug("Request to set booking status for booking with id - {} (approved = {}) is received.", bookingId, approved);
        validateUserExists(userId);
        validateUserOwnItem(userId, bookingId);

        Booking savedBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Booking with id: %d is not found", bookingId)));

        if (approved) {
            validateBookingIsNotApproved(bookingId, savedBooking.getStatus());
            savedBooking.setStatus(BookingStatus.APPROVED);
        } else {
            savedBooking.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(savedBooking);

        log.debug("Booking with ID - {} is {}", updatedBooking.getId(), updatedBooking.getStatus().toString());
        return updatedBooking;
    }

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id: %d is not found", userId));
        }
    }

    private void validateItemIsAvailable(Long itemId) {
        if (!itemRepository.getItemAvailability(itemId)) {
            throw new BadRequestException(String.format("Item with ID - %d is not available", itemId));
        }
    }

    private void validateStartBeforeEnd(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BadRequestException("Booking end should not be before booking end");
        }
    }

    private void validateUserOwnItem(Long userId, Long bookingId) {
        if (!userId.equals(bookingRepository.findItemOwnerIdById(bookingId))) {
            throw new NotFoundException(String.format("Item in booking with ID: %d is not owned by user with ID: %d", bookingId, userId));
        }
    }

    private void validateUserIsNotItemOwner(Long userId, Long ownerId) {
        if (userId.equals(ownerId)) {
            throw new NotFoundException("User cannot book his own item");
        }
    }

    private void validateBookingIsNotApproved(Long bookingId, BookingStatus status) {
        if (BookingStatus.APPROVED.equals(status)) {
            throw new BadRequestException(String.format("Booking with ID: %d is already approved", bookingId));
        }
    }

    private void validateUserOwnItemOrUserCreatedBooking(Long userId, Long bookingId) {
        if (!userId.equals(bookingRepository.findItemOwnerIdById(bookingId)) && !userId.equals(bookingRepository.getBookerId(bookingId))) {
            throw new NotFoundException("Request to get booking is received from user who is not item owner or booker");
        }
    }

    private void validateUserOwnItems(Long userId) {
        if (itemRepository.countItemsOwnedByUser(userId) <= 0) {
            throw new NotFoundException(String.format("User with ID: %d does not own any items", userId));
        }
    }
}
