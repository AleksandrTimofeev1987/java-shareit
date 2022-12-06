package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.BookingCreate;
import ru.practicum.shareit.booking.model.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<BookingCreate, Long> {

    @Query(value = "" +
            "SELECT i.owner_id " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE b.booking_id=?1", nativeQuery = true)
    Long getItemOwnerId(Long bookingId);

    @Query(value = "" +
            "SELECT b.status " +
            "FROM BookingCreate AS b " +
            "WHERE b.id=?1")
    BookingStatus getBookingStatus(Long bookingId);

    @Query(value = "" +
            "SELECT b.bookerId " +
            "FROM BookingCreate AS b " +
            "WHERE b.id=?1")
    Long getBookerId(Long bookingId);

    @Query(value = "" +
            "SELECT * " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "WHERE b.booking_id=?2 AND " +
            "(i.owner_id = ?1 OR b.booker_id = ?1)", nativeQuery = true)
    BookingCreate findByOwnerOrBooker(Long userId, Long bookingId);
}
