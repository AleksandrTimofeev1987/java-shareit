package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    /**
     * Method returns all users (DTO).
     *
     * @return - List of all users (DTO).
     */
    List<UserResponse> getAllUsers();

    /**
     * Method returns user (DTO) by ID.
     *
     * @param id - User ID.
     * @return - User (DTO) with requested ID.
     */
    UserResponse getUserById(Long id);

    /**
     * Method adds user to repository.
     *
     * @param user - User (DTO) to be added.
     * @return - Added user (DTO) with assigned ID.
     */
    UserResponse createUser(UserCreateRequest user);

    /**
     * Method updates user in repository.
     *
     * @param id - ID of user to be updated.
     * @param user - User (DTO) to be updated.
     * @return - Updated user (DTO).
     */
    UserResponse updateUser(Long id, UpdateUserDto user);

    /**
     * Method deletes user in repository.
     *
     * @param id - ID of user to be deleted.
     */
    void deleteUser(Long id);
}
