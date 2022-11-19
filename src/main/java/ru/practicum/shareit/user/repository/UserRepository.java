package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    /**
     * Method returns all users.
     *
     * @return - List of all users.
     */
    List<User> getAllUsers();

    /**
     * Method returns user by ID.
     *
     * @param id - User ID.
     * @return - User with requested ID.
     */
    User getUserById(Long id);

    /**
     * Method adds user to repository.
     *
     * @param user - User to be added.
     * @return - Added user with assigned ID.
     */
    User createUser(User user);

    /**
     * Method updates user in repository.
     *
     * @param id - ID of user to be updated.
     * @param user - User to be updated.
     * @return - Updated user.
     */
    User updateUser(Long id, User user);

    /**
     * Method deletes user in repository.
     *
     * @param id - ID of user to be deleted.
     */
    void deleteUser(Long id);

    /**
     * Method checks if user exists by id.
     *
     * @param id - ID of user to be validated.
     */
    void validateUserExists(Long id);

    /**
     * Method checks if user email is distinct.
     *
     * @param email - email of user to be validated.
     */
    void validateUserEmail(String email);
}
