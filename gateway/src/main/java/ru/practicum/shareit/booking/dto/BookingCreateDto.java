package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Valid
public class BookingCreateDto {

    @NotNull(message = "Booking should include item id")
    private Long itemId;

    @Future(message = "Booking start should be in the future")
    @NotNull(message = "Booking start should not be Null")
    private LocalDateTime start;

    @Future(message = "Booking end should be in the future")
    @NotNull(message = "Booking end should not be Null")
    private LocalDateTime end;
}
