package ru.practicum.shareit.item.validation;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.item.dto.ItemDto;

public class ItemValidator {

    public static void validateItem(ItemDto item) {
        if (StringUtils.isBlank(item.getName())) {
            throw new BadRequestException("Item name should not be Null or Blank");
        }
        if (StringUtils.isBlank(item.getDescription())) {
            throw new BadRequestException("Item description should not be Null or Blank");
        }
        if (item.getDescription().length() > 200) {
            throw new BadRequestException("Item description should be less than 200 characters long");
        }
        if (item.getAvailable() == null) {
            throw new BadRequestException("Item availability should not be null");
        }
    }
}
