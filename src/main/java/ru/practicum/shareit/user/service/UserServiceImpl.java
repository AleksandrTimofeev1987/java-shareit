package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.request.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public List<UserResponse> getAllUsers() {
        log.debug("A list of all users is requested.");
        List<User> allUsers = userRepository.getAllUsers();
        log.debug("A list of all users is received from repository with size of {}.", allUsers.size());
        return allUsers.stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.debug("User with ID - {} is requested.", id);
        userRepository.validateUserExists(id);
        User user = userRepository.getUserById(id);
        log.debug("User with ID - {} is received from repository.", id);
        return mapper.toUserResponse(user);
    }

    @Override
    public UserResponse createUser(UserCreateRequest user) {
        log.debug("Request to add user with name - {} is received.", user.getName());
        userRepository.validateUserEmail(user.getEmail());
        User addedUser = userRepository.createUser(mapper.toUser(user));
        log.debug("User with ID - {} is added to repository.", addedUser.getId());
        return mapper.toUserResponse(addedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserDto user) {
        log.debug("Request to update user with ID - {} is received.", id);
        if (user.getEmail() != null) {
            userRepository.validateUserEmail(user.getEmail());
        }
        User updatedUser = userRepository.updateUser(id, user);
        log.debug("User with ID - {} is updated in repository.", id);
        return mapper.toUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Request to delete user with ID - {} is received.", id);
        userRepository.deleteUser(id);
        log.debug("User with ID - {} is deleted from repository.", id);
    }
}
