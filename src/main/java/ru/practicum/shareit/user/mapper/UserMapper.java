package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toUserFromUserCreate(UserCreateDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void userFromUserUpdate(UserUpdateDto userDto, @MappingTarget User user);
}
