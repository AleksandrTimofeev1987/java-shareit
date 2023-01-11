package ru.practicum.shareit.item.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.entity.User;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    default ItemResponseDto toItemResponseDto(Item item) {
        if (item == null) {
            return null;
        }

        ItemResponseDto itemResponseDto = new ItemResponseDto();

        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setOwner(userToUserResponseDto(item.getOwner()));
        itemResponseDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            itemResponseDto.setRequestId(item.getRequest().getId());
        }

        return itemResponseDto;
    }

    default Item toItem(ItemCreateDto itemDto) {
        if (itemDto == null) {
            return null;
        }

        Item item = new Item();

        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toItemFromItemUpdateDto(ItemUpdateDto itemDto, @MappingTarget Item item);

    default UserResponseDto userToUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        Long id;
        String name;
        String email;

        id = user.getId();
        name = user.getName();
        email = user.getEmail();

        return new UserResponseDto(id, name, email);
    }
}
