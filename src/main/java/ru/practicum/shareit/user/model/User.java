package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "users", schema = "public")
@Valid
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @NotBlank(message = "User name should not be Null or Blank")
    @Column(name = "name", nullable = false)
    private String name;

    @Email(message = "User email should be valid")
    @NotBlank (message = "User email should not be Null or Blank")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
