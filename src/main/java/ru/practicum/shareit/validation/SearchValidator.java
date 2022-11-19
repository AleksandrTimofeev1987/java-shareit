package ru.practicum.shareit.validation;

public class SearchValidator {
    public static boolean validateText(String text) {
        return !text.equals("");
    }
}
