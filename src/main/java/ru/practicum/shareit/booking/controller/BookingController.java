package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper mapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    List<BookingResponseDto> getAllBookingsByBooker(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                    @RequestParam (required = false, defaultValue = "ALL") RequestState state) {
        return bookingService.getAllBookingsByBooker(userId, state)
                .stream()
                .map(mapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    List<BookingResponseDto> getAllBookingsByOwner(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                    @RequestParam (required = false, defaultValue = "ALL") RequestState state) {
        return bookingService.getAllBookingsByOwner(userId, state)
                .stream()
                .map(mapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{bookingId}")
    BookingResponseDto getBookingById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @PathVariable @Min(1L) Long bookingId) {
        return mapper.toBookingResponseDto(bookingService.getBookingById(userId, bookingId));
    }

    @PostMapping
    BookingResponseDto createBooking(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                     @Valid @RequestBody BookingCreateDto bookingDto) {
        BookingEntity booking = mapper.toBookingEntity(bookingDto, userId);

        return mapper.toBookingResponseDto(bookingService.createBooking(userId, booking));
    }

    @PatchMapping("/{bookingId}")
    BookingResponseDto setBookingStatus(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                        @PathVariable @Min(1L) Long bookingId,
                                        @NotNull @RequestParam Boolean approved) {
        return mapper.toBookingResponseDto(bookingService.setBookingStatus(userId, bookingId, approved));
    }
}
