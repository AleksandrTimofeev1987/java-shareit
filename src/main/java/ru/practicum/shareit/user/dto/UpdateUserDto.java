package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Null;

@Getter
@AllArgsConstructor
@Valid
public class UpdateUserDto {

    @Null
    private Long id;

    private String name;

    private String email;
}
