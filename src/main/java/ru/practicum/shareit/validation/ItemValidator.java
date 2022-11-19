package ru.practicum.shareit.validation;

import ru.practicum.shareit.exception.model.item.InvalidItemDescriptionException;
import ru.practicum.shareit.exception.model.item.InvalidItemNameException;
import ru.practicum.shareit.exception.model.item.MissingAvailabilityException;
import ru.practicum.shareit.item.dto.ItemDto;

public class ItemValidator {

    public static void validateItem(ItemDto item) {
        if (item.getName() == null || item.getName().equals("")) {
            throw new InvalidItemNameException("Item name should not be Null or Blank");
        }
        if (item.getDescription() == null || item.getDescription().equals("")) {
            throw new InvalidItemDescriptionException("Item description should not be Null or Blank");
        }
        if (item.getDescription().length() > 200) {
            throw new InvalidItemDescriptionException("Item description should should be less than 200 characters");
        }
        if (item.getAvailable() == null) {
            throw new MissingAvailabilityException("Item availability should not be null");
        }
    }
}
