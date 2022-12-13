package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemResponseWithBookingsAndCommentsDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingShort lastBooking;

    private BookingShort nextBooking;

    private List<Comment> comments;
}
