package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.model.UserShort;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserShort getUserShortById(Long bookerId);

    @Query(value = "" +
            "SELECT u.name " +
            "FROM UserEntity AS u " +
            "WHERE u.id = ?1")
    String getUserNameById(Long authorId);
}
