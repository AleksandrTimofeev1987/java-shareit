package ru.practicum.shareit.booking.entity;

public enum RequestState {

    WAITING("W"),
    REJECTED("R"),
    ALL("A"),
    CURRENT("C"),
    PAST("P"),
    FUTURE("F");

    private String code;

    RequestState(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
