package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponse toItemResponse(Item item);

    Item toItem(ItemCreateRequest itemDto);

    @Mapping(target = "id", ignore = false)
    Item toItem(UpdateItemDto itemDto);
}
