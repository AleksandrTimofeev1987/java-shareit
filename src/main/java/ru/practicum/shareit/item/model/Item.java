package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "items", schema = "public")
@Valid
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long id;

    @NotBlank (message = "Item name should not be Null or Blank")
    @Column(name = "item_name", nullable = false)
    private String name;

    @NotBlank (message = "Item description should not be Null or Blank")
    @Size(max = 200, message = "Maximum length of item description is 200 symbols")
    @Column(name = "item_description", nullable = false)
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @NotNull (message = "Item availability should not be Null")
    @Column(name = "available", nullable = false)
    private Boolean available;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
