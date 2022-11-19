package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.model.user.EmailIsNotDistinctException;
import ru.practicum.shareit.exception.model.user.UserDoesNotExistException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public User updateUser(Long id, User user) {
        User userToBeUpdated = users.get(id);
        if (user.getName() != null) {
            userToBeUpdated.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userToBeUpdated.setEmail(user.getEmail());
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
            throw new UserDoesNotExistException(String.format("User with id: %d is not found", id));
        }
    }

    @Override
    public void validateUserEmail(String email) {
        if (users.values()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList())
                .contains(email)) {
            throw new EmailIsNotDistinctException(String.format("Email: %s is already in use.", email));
        }
    }

    private Long getNextId() {
        return ++globalId;
    }
}
