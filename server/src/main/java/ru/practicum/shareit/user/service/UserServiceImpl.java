package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
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
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        log.debug("A list of all users is requested.");
        List<User> foundUsers = userRepository.findAll();
        log.debug("A list of all users is received from repository with size of {}.", foundUsers.size());
        return foundUsers
                .stream()
                .map(mapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        log.debug("User with ID - {} is requested.", id);
        User foundUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", id)));
        log.debug("User with ID - {} is received from repository.", id);
        return mapper.toUserResponseDto(foundUser);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(User user) {
        log.debug("Request to add user with name - {} is received.", user.getName());

        User createdUser;
        try {
            createdUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Email is a duplicate.");
        }

        log.debug("User with ID - {} is added to repository.", createdUser.getId());
        return mapper.toUserResponseDto(createdUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto userDto) {

        log.debug("Request to update user with ID - {} is received.", id);
        User userForUpgrade = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", id)));
        mapper.toUserFromUserUpdateDto(userDto, userForUpgrade);

        User updatedUser;
        try {
            updatedUser = userRepository.save(userForUpgrade);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Email is a duplicate.");
        }

        log.debug("User with ID - {} is updated in repository.", id);
        return mapper.toUserResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.debug("Request to delete user with ID - {} is received.", id);

        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("User with id: %d is not found", id));
        }
        log.debug("User with ID - {} is deleted from repository.", id);
    }
}
