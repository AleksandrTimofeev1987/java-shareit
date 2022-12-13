package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.UserShort;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Booking {

    private Long id;

    private ItemShort item;

    private UserShort booker;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;
}