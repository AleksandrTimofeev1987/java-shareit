package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(mapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@Min(1L) @PathVariable Long id) {
        return mapper.toUserResponseDto(userService.getUserById(id));
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserCreateDto userDto) {
        User user = mapper.toUserEntity(userDto);
        return mapper.toUserResponseDto(userService.createUser(user));
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@Min(1L) @PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        return mapper.toUserResponseDto(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@Min(1L) @PathVariable Long id) {
        userService.deleteUser(id);
    }
}
