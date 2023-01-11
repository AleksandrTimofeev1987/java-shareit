package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.entity.User;

import java.util.List;

public interface UserService {
    /**
     * Method returns all users.
     *
     * @return List of all users.
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Method returns user by ID.
     *
     * @param id User ID.
     *
     * @return User with requested ID.
     */
    UserResponseDto getUserById(Long id);

    /**
     * Method adds user to repository.
     *
     * @param user User to be added.
     *
     * @return Added user with assigned ID.
     */
    UserResponseDto createUser(User user);

    /**
     * Method updates user in repository.
     *
     * @param id ID of user to be updated.
     * @param userDto User to be updated.
     *
     * @return Updated user.
     */
    UserResponseDto updateUser(Long id, UserUpdateDto userDto);

    /**
     * Method deletes user in repository.
     *
     * @param id ID of user to be deleted.
     */
    void deleteUser(Long id);
}
