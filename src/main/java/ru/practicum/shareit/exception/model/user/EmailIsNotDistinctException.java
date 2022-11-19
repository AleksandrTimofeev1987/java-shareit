package ru.practicum.shareit.exception.model.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EmailIsNotDistinctException extends RuntimeException {
    public EmailIsNotDistinctException(String message) {
        super(message);
    }
}
