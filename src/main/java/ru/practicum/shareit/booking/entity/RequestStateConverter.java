package ru.practicum.shareit.booking.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class RequestStateConverter implements AttributeConverter<RequestState, String> {
    @Override
    public String convertToDatabaseColumn(RequestState requestStatus) {
        if (requestStatus == null) {
            return null;
        }
        return requestStatus.getCode();
    }

    @Override
    public RequestState convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(RequestState.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
