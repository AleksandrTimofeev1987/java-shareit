package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Valid
public class CommentCreateDto {

    private Long id;

    @NotBlank(message = "Comment text should not be null")
    private String text;
}
