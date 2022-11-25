package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validation.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("A list of all users is requested.");
        List<User> allUsers = userRepository.getAllUsers();
        log.debug("A list of all users is received from repository with size of {}.", allUsers.size());
        return allUsers.stream()
                .map(UserMapper::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        log.debug("User with ID - {} is requested.", id);
        userRepository.validateUserExists(id);
        User user = userRepository.getUserById(id);
        log.debug("User with ID - {} is received from repository.", id);
        return UserMapper.mapUserToDto(user);
    }

    @Override
    public UserDto createUser(UserDto user) {
        log.debug("Request to add user with name - {} is received.", user.getName());
        UserValidator.validateUser(user);
        userRepository.validateUserEmail(user.getEmail());
        User addedUser = userRepository.createUser(UserMapper.mapDtoToUser(user));
        log.debug("User with ID - {} is added to repository.", addedUser.getId());
        return UserMapper.mapUserToDto(addedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto user) {
        log.debug("Request to update user with ID - {} is received.", id);
        if (user.getEmail() != null) {
            userRepository.validateUserEmail(user.getEmail());
        }
        User updatedUser = userRepository.updateUser(id, UserMapper.mapDtoToUser(user));
        log.debug("User with ID - {} is updated in repository.", id);
        return UserMapper.mapUserToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Request to delete user with ID - {} is received.", id);
        userRepository.deleteUser(id);
        log.debug("User with ID - {} is deleted from repository.", id);
    }
}
