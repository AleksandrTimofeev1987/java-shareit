package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Valid
public class ItemCreateDto {

    private Long id;

    @NotBlank (message = "Item name should not be Null or Blank")
    private String name;

    @NotBlank (message = "Item description should not be Null or Blank")
    @Size(max = 200, message = "Maximum length of item description is 200 symbols")
    private String description;

    @NotNull (message = "Item availability should not be Null")
    private Boolean available;
}
