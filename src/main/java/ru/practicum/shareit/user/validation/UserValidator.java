package ru.practicum.shareit.user.validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.EmailValidator;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.user.dto.UserDto;

public class UserValidator {

    public static void validateUser(UserDto user) {
        if (StringUtils.isBlank(user.getName())) {
            throw new BadRequestException("User name should not be Null or Blank");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            throw new BadRequestException("User email should not be Null or Blank");
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new BadRequestException("User email is not valid");
        }
    }

}
