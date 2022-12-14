package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.entity.Comment;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default Comment toComment(CommentCreateDto commentDto) {
        if (commentDto == null) {
            return null;
        }

        Comment comment = new Comment();

        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());

        return comment;
    }

    default CommentResponseDto toCommentResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponseDto commentDto = new CommentResponseDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

}
