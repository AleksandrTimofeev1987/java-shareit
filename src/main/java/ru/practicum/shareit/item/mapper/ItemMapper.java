package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponseDto toItemResponseDto(Item item);

    Item toItem(ItemCreateDto itemDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toItemFromItemUpdateDto(ItemUpdateDto itemDto, @MappingTarget Item item);
}
