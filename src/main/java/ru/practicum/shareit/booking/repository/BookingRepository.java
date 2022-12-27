package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime now2);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status);

    List<Booking> findByBookerId(Long userId, Pageable page);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime now, LocalDateTime now1, Pageable page);

    List<Booking> findByBookerIdAndEndIsBefore(Long userId, LocalDateTime now, Pageable page);

    List<Booking> findByBookerIdAndStartIsAfter(Long userId, LocalDateTime now, Pageable page);

    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus waiting, Pageable page);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime now2);

    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status);

    List<Booking> findByItemOwnerId(Long userId, Pageable page);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime now, LocalDateTime now1, Pageable page);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Long userId, LocalDateTime now, Pageable page);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long userId, LocalDateTime now, Pageable page);

    List<Booking> findByItemOwnerIdAndStatus(Long userId, BookingStatus waiting, Pageable page);


    @Query(value = "" +
            "SELECT b.item.owner.id " +
            "FROM Booking AS b " +
            "WHERE b.id = :bookingId")
    Long findItemOwnerIdById(@Param("bookingId") Long bookingId);

    @Query(value = "" +
            "SELECT b.booker.id " +
            "FROM Booking AS b " +
            "WHERE b.id = :bookingId")
    Long getBookerId(@Param("bookingId") Long bookingId);

    Booking findFirstByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Booking findFirstByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    @Query(value = "" +
            "SELECT COUNT(b) " +
            "FROM Booking AS b " +
            "WHERE b.booker.id = :userId AND " +
            "b.item.id = :itemId AND " +
            "b.end < :now")
    Long countByBookerIdItemIdAndPast(@Param("userId") Long userId, @Param("itemId") Long itemId, @Param("now")LocalDateTime now);

}
