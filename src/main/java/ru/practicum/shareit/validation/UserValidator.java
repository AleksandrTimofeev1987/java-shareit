package ru.practicum.shareit.validation;

import org.apache.commons.validator.EmailValidator;
import ru.practicum.shareit.exception.model.user.InvalidUserEmailException;
import ru.practicum.shareit.exception.model.user.InvalidUserNameException;
import ru.practicum.shareit.user.dto.UserDto;

public class UserValidator {

    public static void validateUser(UserDto user) {
        if (user.getName() == null || user.getName().equals("")) {
            throw new InvalidUserNameException("User name should not be Null or Blank");
        }
        if (user.getEmail() == null || user.getEmail().equals("")) {
            throw new InvalidUserEmailException("User email should not be Null or Blank");
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new InvalidUserEmailException("User email is not valid");
        }
    }

}
