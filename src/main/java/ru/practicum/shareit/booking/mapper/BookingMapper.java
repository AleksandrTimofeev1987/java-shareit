package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponseDto toBookingResponseDto(Booking booking);

    default BookingEntity toBookingCreateFromBookingCreateDto(BookingCreateDto bookingDto, long bookerId) {
        if (bookingDto == null) {
            return null;
        }

        BookingEntity booking = new BookingEntity();

        booking.setItemId(bookingDto.getItemId());
        booking.setBookerId(bookerId);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }
}
