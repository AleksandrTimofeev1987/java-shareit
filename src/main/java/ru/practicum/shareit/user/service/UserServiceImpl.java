package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper mapper;

    @Override
    public List<User> getAllUsers() {
        log.debug("A list of all users is requested.");
        List<User> allUsers = userRepository.findAll();
        log.debug("A list of all users is received from repository with size of {}.", allUsers.size());
        return allUsers;
    }

    @Override
    public User getUserById(Long id) {
        log.debug("User with ID - {} is requested.", id);
        Optional<User> userOpt = userRepository.findById(id);
        validateUserExists(id, userOpt);
        log.debug("User with ID - {} is received from repository.", id);
        return userOpt.get();
    }

    @Override
    public User createUser(User user) {
        log.debug("Request to add user with name - {} is received.", user.getName());
//        userValidator.validateUserEmail(user.getEmail());
        User addedUser = userRepository.save(user);
        log.debug("User with ID - {} is added to repository.", addedUser.getId());
        return addedUser;
    }

    // TODO: правильно ли?
    @Override
    public User updateUser(Long id, UserUpdateDto userDto) {

        log.debug("Request to update user with ID - {} is received.", id);
        if (userDto.getEmail() != null) {
            userValidator.validateUserEmail(userDto.getEmail());
        }
        Optional<User> userOpt = userRepository.findById(id);
        validateUserExists(id, userOpt);
        User user = userOpt.get();
        mapper.updateCustomerFromUpdateDto(userDto, user);

        User updatedUser = userRepository.save(user);
        log.debug("User with ID - {} is updated in repository.", id);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Request to delete user with ID - {} is received.", id);
        userRepository.deleteById(id);
        log.debug("User with ID - {} is deleted from repository.", id);
    }

    private void validateUserExists (long id, Optional<User> userOpt) {
        if (!userOpt.isPresent()) {
            throw new NotFoundException(String.format("Item with id: %d is not found", id));
        }
    }

}
