package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Valid
public class BookingCreateDto {

    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
