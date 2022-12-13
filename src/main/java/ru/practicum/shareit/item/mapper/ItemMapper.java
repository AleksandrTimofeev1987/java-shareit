package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.item.model.ItemWithBookingsAndComments;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponseDto toItemResponseDto(ItemEntity item);

    ItemResponseWithBookingsAndCommentsDto toItemResponseWithBookingsAndCommentsDto(ItemWithBookingsAndComments item);

    ItemEntity toItemEntity(ItemCreateDto itemDto);

    ItemShortDto toItemShortDto(ItemShort item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toItemEntityFromItemUpdateDto(ItemUpdateDto itemDto, @MappingTarget ItemEntity item);


}
