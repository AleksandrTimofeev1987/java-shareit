package ru.practicum.shareit.item.validation;

public class SearchValidator {
    public static boolean validateText(String text) {
        return !text.equals("");
    }
}
