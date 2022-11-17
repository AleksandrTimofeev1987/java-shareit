package ru.practicum.shareit.validation;

import ru.practicum.shareit.exception.model.InvalidUserName;
import ru.practicum.shareit.user.model.User;

public class UserValidator {

    public static void validateUser(User user) {
        if (user.getName().equals(null) || user.getName().equals("")) {
            throw new InvalidUserName("User name should not be Null or Blank");
        }
        if (user.getEmail().equals(null) || user.getEmail().equals("")) {
            throw new InvalidUserName("User email should not be Null or Blank");
        }
    }

}
