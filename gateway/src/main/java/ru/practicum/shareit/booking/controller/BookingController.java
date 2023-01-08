package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.RequestState;
import ru.practicum.shareit.exception.model.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByBooker(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                         @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting all bookings by booker with userId={}", userId);
        RequestState state = RequestState.from(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
        validatePaginationParameters(from, size);
        return bookingClient.getAllBookingsByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting all bookings by owner with userId={}", userId);
        RequestState state = RequestState.from(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
        validatePaginationParameters(from, size);
        return bookingClient.getAllBookingsByOwner(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                 @Positive @PathVariable Long bookingId) {
        log.info("Getting bookings by bookingId={} userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                @Valid @RequestBody BookingCreateDto bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        validateStartBeforeEnd(bookingDto);
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setBookingStatus(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                   @Positive @PathVariable Long bookingId,
                                                   @NotNull @RequestParam Boolean approved) {
        return bookingClient.setBookingStatus(userId, bookingId, approved);
    }

    private void validatePaginationParameters(Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new BadRequestException("Incorrect pagination parameters.");
        }
    }

    private void validateStartBeforeEnd(BookingCreateDto booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BadRequestException("Booking end should not be before booking end");
        }
    }
}
