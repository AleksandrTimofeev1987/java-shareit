package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Valid
public class ItemRequestCreateDto {

    private Long id;

    @NotBlank(message = "Item request description should not be null")
    private String description;
}
