package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
public class BookingRepositoryTest {

    private User genericUser;
    private Item genericItem;
    private User genericOwner;
    private Booking genericBooking;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void beforeEach() {
        genericUser = new User();
        genericUser.setId(1L);
        genericUser.setName("name");
        genericUser.setEmail("email@mail.ru");

        genericOwner = new User();
        genericOwner.setId(2L);
        genericOwner.setName("owner");
        genericOwner.setEmail("owner@mail.ru");

        genericItem = new Item();
        genericItem.setId(1L);
        genericItem.setName("name");
        genericItem.setDescription("description");
        genericItem.setOwner(genericOwner);
        genericItem.setAvailable(true);

        genericBooking = new Booking();
        genericBooking.setId(1L);
        genericBooking.setItem(genericItem);
        genericBooking.setBooker(genericUser);
        genericBooking.setStart(LocalDateTime.now().plusSeconds(1));
        genericBooking.setEnd(LocalDateTime.now().plusSeconds(2));
        genericBooking.setStatus(BookingStatus.WAITING);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testFindItemOwnerIdById() {
        //given
        userRepository.save(genericUser);
        userRepository.save(genericOwner);
        itemRepository.save(genericItem);
        bookingRepository.save(genericBooking);

        //when
        Long result = bookingRepository.findItemOwnerIdById(1L);

        //then
        Assertions.assertEquals(2L, result);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetBookerId() {
        //given
        userRepository.save(genericUser);
        userRepository.save(genericOwner);
        itemRepository.save(genericItem);
        bookingRepository.save(genericBooking);

        //when
        Long result = bookingRepository.getBookerId(1L);

        //then
        Assertions.assertEquals(1L, result);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testCountByBookerIdItemIdAndPast() throws InterruptedException {
        //given
        userRepository.save(genericUser);
        userRepository.save(genericOwner);
        itemRepository.save(genericItem);
        bookingRepository.save(genericBooking);
        Thread.sleep(2000);

        //when
        Long result = bookingRepository.countByBookerIdItemIdAndPast(1L, 1L, LocalDateTime.now());

        //then
        Assertions.assertEquals(1L, result);
    }
}
