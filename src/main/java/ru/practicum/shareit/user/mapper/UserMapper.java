package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    User toUserFromUserCreate(UserCreateRequest userDto);

    User toUserFromUserUpdate(UserUpdateDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromUpdateDto(UserUpdateDto userDto, @MappingTarget User user);
}
