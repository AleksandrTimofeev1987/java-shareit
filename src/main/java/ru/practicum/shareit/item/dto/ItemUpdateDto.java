package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Null;

@Getter
@AllArgsConstructor
@Valid
public class ItemUpdateDto {

    @Null
    private Long id;

    private String name;

    private String description;

    @Null
    private Long ownerId;

    private Boolean available;
}
