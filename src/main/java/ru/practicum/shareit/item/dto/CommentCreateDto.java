package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Valid
public class CommentCreateDto {

    @NotBlank(message = "Comment text should not be null")
    private String text;

    private LocalDateTime created;
}
