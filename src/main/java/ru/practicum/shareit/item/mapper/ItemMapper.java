package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseWithBookingsAndCommentsDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingsAndComments;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponseDto toItemResponseDto(Item item);

    ItemResponseWithBookingsAndCommentsDto toItemResponseWithBookingsAndCommentsDto(ItemWithBookingsAndComments item);

    Item toItemFromItemCreateDto(ItemCreateDto itemDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void itemFromItemUpdateDto(ItemUpdateDto itemDto, @MappingTarget Item item);
}
