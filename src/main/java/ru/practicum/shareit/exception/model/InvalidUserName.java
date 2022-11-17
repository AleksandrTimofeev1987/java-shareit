package ru.practicum.shareit.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidUserName extends RuntimeException {
    public InvalidUserName(String message) {
        super(message);
    }
}
