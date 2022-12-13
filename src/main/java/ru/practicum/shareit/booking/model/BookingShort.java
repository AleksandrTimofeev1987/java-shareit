package ru.practicum.shareit.booking.model;

import java.time.LocalDateTime;

public interface BookingShort {
    Long getId();

    Long getBookerId();

    LocalDateTime getStart();

    LocalDateTime getEnd();
}
