package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.model.*;

@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BadRequestException.class})
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

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotExists(final NotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(final ForbiddenException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExists(final ConflictException e) {
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
