package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.UserShort;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingResponseDto {

    private Long id;

    private ItemShort item;

    private UserShort booker;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;
}
