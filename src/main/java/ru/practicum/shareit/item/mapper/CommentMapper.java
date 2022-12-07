package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentEntity;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponseDto toCommentResponseDto(Comment comment);

    default CommentEntity toCommentCreateFromCommentCreateDto(CommentCreateDto commentDto, long authorId, long itemId) {
        if (commentDto == null) {
            return null;
        }

        CommentEntity comment = new CommentEntity();

        comment.setAuthorId(authorId);
        comment.setCreated(LocalDateTime.now());
        comment.setText(commentDto.getText());
        comment.setItemId(itemId);

        return comment;
    }
}
