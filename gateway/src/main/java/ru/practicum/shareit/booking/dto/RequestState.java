package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum RequestState {

    WAITING,
    REJECTED,
    ALL,
    CURRENT,
    PAST,
    FUTURE;

    public static Optional<RequestState> from(String stringState) {
        for (RequestState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
