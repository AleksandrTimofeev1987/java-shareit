package ru.practicum.shareit.booking.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class RequestStatusConverter implements AttributeConverter<RequestStatus, String> {
    @Override
    public String convertToDatabaseColumn(RequestStatus requestStatus) {
        if (requestStatus == null) {
            return null;
        }
        return requestStatus.getCode();
    }

    @Override
    public RequestStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(RequestStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
