package ru.practicum.shareit.item.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {

    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;

}
