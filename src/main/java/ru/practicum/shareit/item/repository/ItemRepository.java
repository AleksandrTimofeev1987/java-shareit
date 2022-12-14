package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long userId);

    @Query(value = "" +
            "SELECT i " +
            "FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND i.available = true")
    List<Item> searchByNameOrDescription(String text);

    @Query(value = "" +
            "SELECT i.available " +
            "FROM Item AS i " +
            "WHERE i.id = ?1")
    boolean getItemAvailability(Long itemId);

    @Query(value = "" +
            "SELECT COUNT(i) " +
            "FROM Item AS i " +
            "WHERE i.owner.id = ?1")
    Long countItemsOwnedByUser(Long userId);
}
