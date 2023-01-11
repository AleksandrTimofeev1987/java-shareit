package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest);

    default ItemRequest toItemRequest(ItemRequestCreateDto itemRequestDto) {
        if (itemRequestDto == null) {
            return null;
        }

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequest;
    }
}
