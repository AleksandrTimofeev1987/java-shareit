package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private User genericUser = new User();
    private final UserResponseDto genericUserResponseDto = new UserResponseDto(1L, "name", "email@mail.ru");
    @InjectMocks
    private UserServiceImpl service;

    @Mock
    UserRepository repository;

    @Mock
    UserMapper mapper;

    @BeforeEach
    public void beforeEach() {
        genericUser.setId(1L);
        genericUser.setName("name");
        genericUser.setEmail("email@mail.ru");
    }

    @Test
    public void testGetAllUsers() {
        List<User> foundUsers = new ArrayList<>();
        foundUsers.add(genericUser);
        List<UserResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericUserResponseDto);

        Mockito
                .when(repository.findAll())
                .thenReturn(foundUsers);

        Mockito
                .when(mapper.toUserResponseDto(genericUser))
                .thenReturn(genericUserResponseDto);

        List<UserResponseDto> receivedList = service.getAllUsers();
        Assertions.assertEquals(listToGet, receivedList);
    }

    @Test
    public void testGetUserById() {
        Mockito
                .when(repository.findById(1L))
                .thenReturn(Optional.of(genericUser));

        Mockito
                .when(mapper.toUserResponseDto(genericUser))
                .thenReturn(genericUserResponseDto);

        UserResponseDto result = service.getUserById(1L);
        Assertions.assertEquals(genericUserResponseDto, result);
    }

    @Test
    public void testCreateUser() {
        Mockito
                .when(repository.save(genericUser))
                .thenReturn(genericUser);

        Mockito
                .when(mapper.toUserResponseDto(genericUser))
                .thenReturn(genericUserResponseDto);

        UserResponseDto savedUser = service.createUser(genericUser);
        Assertions.assertEquals(genericUserResponseDto, savedUser);
    }

    @Test
    public void testUpdateUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto(null, "updated_name", "updated_email@mail.ru");
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("updated_name");
        updatedUser.setEmail("updated_email@mail.ru");
        UserResponseDto userResponseDto = new UserResponseDto(1L, "updated_name", "updated_email@mail.ru");

        Mockito
                .when(repository.findById(1L))
                .thenReturn(Optional.of(genericUser));

        Mockito
                .when(repository.save(Mockito.any(User.class)))
                .thenReturn(updatedUser);

        Mockito
                .when(mapper.toUserResponseDto(updatedUser))
                .thenReturn(userResponseDto);

        UserResponseDto userResult = service.updateUser(1L, userUpdateDto);
        Assertions.assertEquals(userResponseDto, userResult);
    }
}
