package ru.practicum.shareit.booking.entity;

public enum BookingStatus {
    WAITING("W"), APPROVED("A"), REJECTED("R");

    private String code;

    BookingStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}