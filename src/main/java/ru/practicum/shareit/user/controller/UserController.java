package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
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
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@Min(1L) @PathVariable Long id) {
        return mapper.toUserResponse(userService.getUserById(id));
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest userDto) {
        User user = mapper.toUserFromUserCreate(userDto);
        return mapper.toUserResponse(userService.createUser(user));
    }

    @PatchMapping("/{id}")
    public UserResponse updateUser(@Min(1L) @PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        return mapper.toUserResponse(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@Min(1L) @PathVariable Long id) {
        userService.deleteUser(id);
    }
}
