package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.booking.entity.RequestState;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private static final Sort SORT_BY_START = Sort.by(Sort.Direction.DESC, "start");
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingsByBooker(Long userId, RequestState state, Integer from, Integer size) {
        log.debug("Request to get all bookings made by user with id - {} is received (state = {}).", userId, state);
        validateUserExists(userId);

        List<Booking> foundBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        if (from == null || size == null) {
            foundBookings = getBookingsByBooker(userId, state, foundBookings, now);
        } else {
            validatePaginationParameters(from, size);
            Pageable page = PageRequest.of(from / size, size, SORT_BY_START);
            foundBookings = getBookingsByBookerPage(userId, state, foundBookings, now, page);
        }

        log.debug("Found all bookings made by user with id - {} in amount of {}.", userId, foundBookings.size());
        return foundBookings
                .stream()
                .map(mapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingsByOwner(Long userId, RequestState state, Integer from, Integer size) {
        log.debug("Request to get all bookings for items owned by user with id - {} is received (state = {}).", userId, state);
        validateUserExists(userId);
        validateUserOwnItems(userId);

        List<Booking> foundBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        if (from == null || size == null) {
            foundBookings = getBookingsByOwner(userId, state, foundBookings, now);
        } else {
            validatePaginationParameters(from, size);
            Pageable page = PageRequest.of(from / size, size, SORT_BY_START);
            foundBookings = getBookingsByOwnerPage(userId, state, foundBookings, now, page);
        }

        log.debug("Found all bookings for items owned by user with id - {} in amount of {}.", userId, foundBookings.size());
        return foundBookings
                .stream()
                .map(mapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long userId, Long bookingId) {
        log.debug("Request to get booking with id - {} from user with id - {} is received.", bookingId, userId);
        validateUserExists(userId);
        validateUserOwnItemOrUserCreatedBooking(userId, bookingId);

        Booking foundBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Booking with id: %d is not found", bookingId)));

        log.debug("Booking with ID - {} is found.", foundBooking.getId());
        return mapper.toBookingResponseDto(foundBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto createBooking(Long userId, Booking booking) {
        log.debug("Request to add booking for item with id - {} is received.", booking.getItem().getId());
        validateStartBeforeEnd(booking);

        Item bookedItem = itemRepository.findById(booking.getItem().getId()).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", booking.getItem().getId())));
        validateItemIsAvailable(bookedItem);
        validateUserIsNotItemOwner(userId, bookedItem.getOwner().getId());

        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId)));

        booking.setItem(bookedItem);
        booking.setBooker(booker);

        Booking createdBooking = bookingRepository.save(booking);

        log.debug("Booking with ID - {} is added to repository.", createdBooking.getId());
        return mapper.toBookingResponseDto(createdBooking);
    }


    @Override
    @Transactional
    public BookingResponseDto setBookingStatus(Long userId, Long bookingId, Boolean approved) {
        log.debug("Request to set booking status for booking with id - {} (approved = {}) is received.", bookingId, approved);
        validateUserExists(userId);
        validateUserOwnItem(userId, bookingId);

        Booking bookingForUpdate = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Booking with id: %d is not found", bookingId)));

        if (approved) {
            validateBookingIsNotApproved(bookingId, bookingForUpdate.getStatus());
            bookingForUpdate.setStatus(BookingStatus.APPROVED);
        } else {
            bookingForUpdate.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(bookingForUpdate);

        log.debug("Booking with ID - {} is {}", updatedBooking.getId(), updatedBooking.getStatus().toString());
        return mapper.toBookingResponseDto(updatedBooking);
    }

    private List<Booking> getBookingsByBooker(Long userId, RequestState state, List<Booking> foundBookings, LocalDateTime now) {
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
        return foundBookings;
    }

    private List<Booking> getBookingsByBookerPage(Long userId, RequestState state, List<Booking> foundBookings, LocalDateTime now, Pageable page) {
        switch (state) {
            case ALL:
                foundBookings = bookingRepository.findByBookerId(userId, page);
                break;
            case CURRENT:
                foundBookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, now, now, page);
                break;
            case PAST:
                foundBookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, now, page);
                break;
            case FUTURE:
                foundBookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, now, page);
                break;
            case WAITING:
                foundBookings = bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                foundBookings = bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, page);
                break;
        }
        return foundBookings;
    }

    private List<Booking> getBookingsByOwner(Long userId, RequestState state, List<Booking> foundBookings, LocalDateTime now) {
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
        return foundBookings;
    }

    private List<Booking> getBookingsByOwnerPage(Long userId, RequestState state, List<Booking> foundBookings, LocalDateTime now, Pageable page) {
        switch (state) {
            case ALL:
                foundBookings = bookingRepository.findByItemOwnerId(userId, page);
                break;
            case CURRENT:
                foundBookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId, now, now, page);
                break;
            case PAST:
                foundBookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, now, page);
                break;
            case FUTURE:
                foundBookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, now, page);
                break;
            case WAITING:
                foundBookings = bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                foundBookings = bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED, page);
                break;
        }
        return foundBookings;
    }

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id: %d is not found", userId));
        }
    }

    private void validateItemIsAvailable(Item bookedItem) {
        if (!bookedItem.getAvailable()) {
            throw new BadRequestException(String.format("Item with ID - %d is not available", bookedItem.getId()));
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

    private void validatePaginationParameters(Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new BadRequestException("Incorrect pagination parameters.");
        }
    }
}
