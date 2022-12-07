package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Comment {

    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;

}
