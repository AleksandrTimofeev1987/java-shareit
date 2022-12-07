package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query(value = "" +
            "SELECT i.owner_id " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE b.booking_id=?1", nativeQuery = true)
    Long getItemOwnerId(Long bookingId);

    @Query(value = "" +
            "SELECT b.status " +
            "FROM BookingEntity AS b " +
            "WHERE b.id=?1")
    BookingStatus getBookingStatus(Long bookingId);

    @Query(value = "" +
            "SELECT b.bookerId " +
            "FROM BookingEntity AS b " +
            "WHERE b.id=?1")
    Long getBookerId(Long bookingId);

    @Query(value = "" +
            "SELECT * " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE b.booking_id=?2 AND " +
            "(i.owner_id = ?1 OR b.booker_id = ?1)", nativeQuery = true)
    BookingEntity findByOwnerOrBooker(Long userId, Long bookingId);

    List<BookingEntity> findByBookerId(Long userId, Sort sort);

    List<BookingEntity> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long userId, Sort start, LocalDateTime now, LocalDateTime now2);

    List<BookingEntity> findByBookerIdAndEndIsBefore(Long userId, Sort sort, LocalDateTime now);

    List<BookingEntity> findByBookerIdAndStartIsAfter(Long userId, Sort sort, LocalDateTime now);

    List<BookingEntity> findByBookerIdAndStatus(Long userId, Sort sort, BookingStatus status);

    @Query(value = "" +
            "SELECT * " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE i.owner_id = ?1 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<BookingEntity> findAllByOwnerId(Long userId);

    @Query(value = "" +
            "SELECT * " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.end_date > ?2 " +
            "AND b.start_date < ?2 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<BookingEntity> findByOwnerIdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime now);

    @Query(value = "" +
            "SELECT * " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.end_date < ?2 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<BookingEntity> findByOwnerIdAndEndIsBefore(Long userId, LocalDateTime now);

    @Query(value = "" +
            "SELECT * " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.start_date > ?2 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<BookingEntity> findByOwnerIdAndStartIsAfter(Long userId, LocalDateTime now);

    @Query(value = "" +
            "SELECT * " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE i.owner_id = ?1 AND " +
            "b.status = ?2 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<BookingEntity> findByItemOwnerIdAndStatus(Long userId, String state);

    List<BookingShort> findByItemId(Long id);

    @Query(value = "" +
            "SELECT b.id " +
            "FROM BookingEntity AS b " +
            "WHERE b.bookerId = ?1 AND " +
            "b.itemId = ?2 AND " +
            "b.end < ?3")
    List<Long> findByBookerIdItemIdAndPast(Long userId, Long itemId, LocalDateTime now);
}
