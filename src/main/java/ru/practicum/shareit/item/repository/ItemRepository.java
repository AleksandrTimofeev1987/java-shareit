package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.model.ItemShort;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    List<ItemEntity> findByOwnerId(Long ownerId);

    @Query(value = "" +
            "SELECT i " +
            "FROM ItemEntity AS i " +
            "WHERE LOWER (i.name) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "OR LOWER (i.description) LIKE LOWER(CONCAT('%', ?1, '%'))" +
            "AND i.available = true")
    List<ItemEntity> searchItemsByText(String text);

    @Query(value = "" +
            "SELECT i.available " +
            "FROM ItemEntity AS i " +
            "WHERE i.id = ?1")
    boolean getItemAvailability(Long itemId);

    ItemShort getItemShortById(Long itemId);

    @Query(value = "" +
            "SELECT i.ownerId " +
            "FROM ItemEntity AS i " +
            "WHERE i.id = ?1")
    Long getItemOwnerId(Long itemId);

    @Query(value = "" +
            "SELECT COUNT(i) " +
            "FROM ItemEntity AS i " +
            "WHERE i.ownerId = ?1")
    Long countItemsOwnedByUser(Long userId);
}
