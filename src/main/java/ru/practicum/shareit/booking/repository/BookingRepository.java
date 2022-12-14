package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long userId, Sort sort);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long userId, Sort start, LocalDateTime now, LocalDateTime now2);

    List<Booking> findByBookerIdAndEndIsBefore(Long userId, Sort sort, LocalDateTime now);

    List<Booking> findByBookerIdAndStartIsAfter(Long userId, Sort sort, LocalDateTime now);

    List<Booking> findByBookerIdAndStatus(Long userId, Sort sort, BookingStatus status);

    List<Booking> findByItemOwnerId(Long userId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long userId, Sort start, LocalDateTime now, LocalDateTime now2);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Long userId, Sort sort, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long userId, Sort sort, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStatus(Long userId, Sort sort, BookingStatus status);


    @Query(value = "" +
            "SELECT b.item.owner.id " +
            "FROM Booking AS b " +
            "WHERE b.id=?1")
    Long findItemOwnerIdById(Long bookingId);

    @Query(value = "" +
            "SELECT b.booker.id " +
            "FROM Booking AS b " +
            "WHERE b.id=?1")
    Long getBookerId(Long bookingId);

    Booking findFirstByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Booking findFirstByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    @Query(value = "" +
            "SELECT COUNT(b) " +
            "FROM Booking AS b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.item.id = ?2 AND " +
            "b.end < ?3")
    Long countByBookerIdItemIdAndPast(Long userId, Long itemId, LocalDateTime now);
}
