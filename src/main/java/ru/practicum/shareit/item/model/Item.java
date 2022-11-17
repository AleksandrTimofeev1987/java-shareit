package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class Item {
    private Long id;

    @NotBlank(message = "Item name should not be Null or Blank.")
    private String name;

    @NotBlank(message = "Item description should not be Null or Blank.")
    @Size(max = 200, message = "The description should contain no more than 200 characters")
    private String description;

    @NotNull(message = "Item owner should be filled.")
    private Long ownerId;

    @NotNull(message = "Item availability should be filled.")
    private Boolean isAvailable;
}
