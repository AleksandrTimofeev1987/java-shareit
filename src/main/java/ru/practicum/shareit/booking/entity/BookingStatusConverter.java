package ru.practicum.shareit.booking.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class BookingStatusConverter implements AttributeConverter<BookingStatus, String> {

    @Override
    public String convertToDatabaseColumn(BookingStatus bookingStatus) {
        if (bookingStatus == null) {
            return null;
        }
        return bookingStatus.getCode();
    }

    @Override
    public BookingStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(BookingStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
