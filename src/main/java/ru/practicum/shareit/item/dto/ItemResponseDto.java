package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {

    private Long id;

    private String name;

    private String description;

    private UserResponseDto owner;

    private Boolean available;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private Set<CommentResponseDto> comments = new HashSet<>();
}
