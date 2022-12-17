package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.RequestState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper mapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    public List<BookingResponseDto> getAllBookingsByBooker(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                    @RequestParam (required = false, defaultValue = "ALL") RequestState state) {
        return bookingService.getAllBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsByOwner(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                    @RequestParam (required = false, defaultValue = "ALL") RequestState state) {
        return bookingService.getAllBookingsByOwner(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @PathVariable @Min(1L) Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                     @Valid @RequestBody BookingCreateDto bookingDto) {
        Booking booking = mapper.toBooking(bookingDto);

        return bookingService.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto setBookingStatus(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                        @PathVariable @Min(1L) Long bookingId,
                                        @NotNull @RequestParam Boolean approved) {
        return bookingService.setBookingStatus(userId, bookingId, approved);
    }
}
