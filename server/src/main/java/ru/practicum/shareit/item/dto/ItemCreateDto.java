package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@Valid
public class ItemCreateDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;
}
