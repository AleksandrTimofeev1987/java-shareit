package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerIdOrderById(Long userId);

    List<Item> findByOwnerId(Long userId, Pageable page);

    @Query(value = "" +
            "SELECT i " +
            "FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND i.available = true")
    List<Item> searchByNameOrDescription(@Param("text") String text);

    @Query(value = "" +
            "SELECT i " +
            "FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND i.available = true")
    List<Item> searchByNameOrDescription(@Param("text") String text, Pageable page);

    @Query(value = "" +
            "SELECT COUNT(i) " +
            "FROM Item AS i " +
            "WHERE i.owner.id = :userId")
    Long countItemsOwnedByUser(@Param("userId") Long userId);

    List<Item> findAllByRequestId(Long id);


}
