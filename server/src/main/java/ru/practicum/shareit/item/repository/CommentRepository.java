package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.entity.Comment;

import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findByItemId(Long id);
}
