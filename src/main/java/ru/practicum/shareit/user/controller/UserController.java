package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@Min(1L) @PathVariable Long id) {
        return service.getUserById(id);
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserCreateDto userDto) {
        User user = mapper.toUserEntity(userDto);
        return service.createUser(user);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@Min(1L) @PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        return service.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@Min(1L) @PathVariable Long id) {
        service.deleteUser(id);
    }
}
