package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto mapItemToDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getOwnerId(), item.getIsAvailable());
    }

    public static Item mapDtoToItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getOwnerId(), itemDto.getAvailable());
    }
}
