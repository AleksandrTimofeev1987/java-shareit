package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Null;

@Getter
@AllArgsConstructor
@Valid
public class UserUpdateDto {

    @Null
    private Long id;

    private String name;

    @Email(message = "User email should be valid")
    private String email;
}
