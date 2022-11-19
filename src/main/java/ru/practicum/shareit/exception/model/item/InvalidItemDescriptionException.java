package ru.practicum.shareit.exception.model.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidItemDescriptionException extends RuntimeException {
    public InvalidItemDescriptionException(String message) {
        super(message);
    }
}
