package ru.practicum.shareit.exception.model.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingAvailabilityException extends RuntimeException {
    public MissingAvailabilityException(String message) {
        super(message);
    }
}
