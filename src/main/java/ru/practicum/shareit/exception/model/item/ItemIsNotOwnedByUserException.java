package ru.practicum.shareit.exception.model.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ItemIsNotOwnedByUserException extends RuntimeException {
    public ItemIsNotOwnedByUserException(String message) {
        super(message);
    }
}
