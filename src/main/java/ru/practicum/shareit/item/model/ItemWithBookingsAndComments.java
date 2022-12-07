package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingShort;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithBookingsAndComments {
    private Long id;

    private String name;

    private String description;

    private Long ownerId;

    private Boolean available;

    private BookingShort lastBooking;

    private BookingShort nextBooking;

    private List<Comment> comments;
}
