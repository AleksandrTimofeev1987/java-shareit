package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.UserEntity;

import java.util.List;

public interface UserService {
    /**
     * Method returns all users.
     *
     * @return - List of all users.
     */
    List<UserEntity> getAllUsers();

    /**
     * Method returns user by ID.
     *
     * @param id - User ID.
     * @return - User with requested ID.
     */
    UserEntity getUserById(Long id);

    /**
     * Method adds user to repository.
     *
     * @param user - User to be added.
     * @return - Added user with assigned ID.
     */
    UserEntity createUser(UserEntity user);

    /**
     * Method updates user in repository.
     *
     * @param id - ID of user to be updated.
     * @param userDto - User to be updated.
     * @return - Updated user.
     */
    UserEntity updateUser(Long id, UserUpdateDto userDto);

    /**
     * Method deletes user in repository.
     *
     * @param id - ID of user to be deleted.
     */
    void deleteUser(Long id);
}
