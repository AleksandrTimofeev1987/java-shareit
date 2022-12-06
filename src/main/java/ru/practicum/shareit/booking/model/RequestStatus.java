package ru.practicum.shareit.booking.model;

public enum RequestStatus {

    WAITING("W"),
    REJECTED("R"),
    ALL("A"),
    CURRENT("C"),
    PAST("P"),
    FUTURE("F");

    private String code;

    RequestStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
