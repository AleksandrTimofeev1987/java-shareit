package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    /**
     * Method returns all users (DTO).
     *
     * @return - List of all users (DTO).
     */
    List<User> getAllUsers();

    /**
     * Method returns user (DTO) by ID.
     *
     * @param id - User ID.
     * @return - User (DTO) with requested ID.
     */
    User getUserById(Long id);

    /**
     * Method adds user to repository.
     *
     * @param user - User (DTO) to be added.
     * @return - Added user (DTO) with assigned ID.
     */
    User createUser(User user);

    /**
     * Method updates user in repository.
     *
     * @param id - ID of user to be updated.
     * @param userDto - User (DTO) to be updated.
     * @return - Updated user (DTO).
     */
    User updateUser(Long id, UserUpdateDto userDto);

    /**
     * Method deletes user in repository.
     *
     * @param id - ID of user to be deleted.
     */
    void deleteUser(Long id);
}
