package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponseDto toBookingResponseDto(Booking booking);

    default BookingShortDto toBookingShortDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingShortDto result = new BookingShortDto();

        result.setId(booking.getId());
        result.setBookerId(booking.getBooker().getId());
        result.setStart(booking.getStart());
        result.setEnd(booking.getEnd());

        return result;
    }

    default Booking toBooking(BookingCreateDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }

        Booking booking = new Booking();
        Item item = new Item();
        item.setId(bookingDto.getItemId());

        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }
}
