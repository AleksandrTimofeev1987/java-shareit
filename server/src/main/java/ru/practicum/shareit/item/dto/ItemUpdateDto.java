package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Null;

@Getter
@AllArgsConstructor
public class ItemUpdateDto {

    @Null
    private Long id;

    private String name;

    private String description;

    private Boolean available;
}
