package ru.practicum.shareit.user.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;

    public void validateUserExists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("User with id: %d is not found", id));
        }
    }

    public void validateUserEmail(String email) {
        // TODO: упростить запрос
        if (!userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList())
                .contains(email));
    }

}
