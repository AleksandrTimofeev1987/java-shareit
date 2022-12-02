package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.request.model.User;
import ru.practicum.shareit.user.dto.UpdateUserDto;

import java.util.*;
import java.util.stream.Collectors;

@Repository("InMemoryUserRepository")
public class InMemoryUserRepository implements UserRepository {

    private Map<Long, User> users = new HashMap<>();
    private Long globalId = 0L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        final long id = getNextId();
        user.setId(id);
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User updateUser(Long id, UpdateUserDto user) {
        User storedUser = users.get(id);

        if (Objects.isNull(storedUser)) {
            return null;
        }

        if (user.getName() != null && !Objects.equals(storedUser.getName(), user.getName())) {
            storedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !Objects.equals(storedUser.getEmail(), user.getEmail())) {
            storedUser.setEmail(user.getEmail());
        }
        return users.get(id);
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public void validateUserExists(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User with id: %d is not found", id));
        }
    }

    @Override
    public void validateUserEmail(String email) {
        if (users.values()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList())
                .contains(email)) {
            throw new ConflictException(String.format("Email: %s is already in use.", email));
        }
    }

    private Long getNextId() {
        return ++globalId;
    }
}
