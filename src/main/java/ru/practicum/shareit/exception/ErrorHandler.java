package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.model.*;
import ru.practicum.shareit.exception.model.item.*;
import ru.practicum.shareit.exception.model.user.EmailIsNotDistinctException;
import ru.practicum.shareit.exception.model.user.InvalidUserEmailException;
import ru.practicum.shareit.exception.model.user.InvalidUserNameException;
import ru.practicum.shareit.exception.model.user.UserDoesNotExistException;

@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class,
            InvalidUserNameException.class,
            InvalidUserEmailException.class,
            InvalidItemNameException.class,
            InvalidItemDescriptionException.class,
            MissingAvailabilityException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final RuntimeException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeader(final MissingRequestHeaderException e) {
        String message = "Missing request header 'X-Sharer-User-Id'";
        log.warn(message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(value = {UserDoesNotExistException.class, ItemDoesNotExistException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotExists(final RuntimeException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(final ItemIsNotOwnedByUserException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExists(final EmailIsNotDistinctException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        String message = "Unexpected error has occurred.";
        log.warn(message);
        return new ErrorResponse(message);
    }
}
