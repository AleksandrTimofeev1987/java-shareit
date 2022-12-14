package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingResponseDto {

    private Long id;

    private ItemShortDto item;

    private UserShortDto booker;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;
}
