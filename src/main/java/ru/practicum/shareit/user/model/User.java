package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User {

    private Long id;

    private String name;

    @Email(message = "Invalid e-mail.")
    private String email;
}
