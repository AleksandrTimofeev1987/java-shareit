package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Valid
public class ItemUpdateDto {

    @Null
    private Long id;

    @Size(max = 200, message = "Maximum length of item name is 200 symbols")
    private String name;

    @Size(max = 200, message = "Maximum length of item description is 200 symbols")
    private String description;

    private Boolean available;
}
