package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.booking.entity.RequestState;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    private User genericUser;
    private User genericOwner;
    private Item genericItem;
    private Booking genericBooking;
    private BookingResponseDto genericBookingResponseDto;

    @InjectMocks
    private BookingServiceImpl service;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper mapper;

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

        genericBookingResponseDto = new BookingResponseDto(1L, new ItemShortDto(1L, "name"), new UserShortDto(1L, "name"), LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), BookingStatus.WAITING);

        genericBooking = new Booking();
        genericBooking.setId(1L);
        genericBooking.setItem(genericItem);
        genericBooking.setBooker(genericUser);
        genericBooking.setStart(genericBookingResponseDto.getStart());
        genericBooking.setEnd(genericBookingResponseDto.getEnd());
        genericBooking.setStatus(BookingStatus.WAITING);
    }

    @Test
    public void testGetAllBookingsByBooker() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findByBookerIdOrderByStartDesc(1L))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByBooker(1L, RequestState.ALL, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetCurrentBookingsByBooker() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByBooker(1L, RequestState.CURRENT, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetPastBookingsByBooker() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByBooker(1L, RequestState.PAST, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetFutureBookingsByBooker() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByBooker(1L, RequestState.FUTURE, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetWaitingBookingsByBooker() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(1L, BookingStatus.WAITING))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByBooker(1L, RequestState.WAITING, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetRejectedBookingsByBooker() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(1L, BookingStatus.REJECTED))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByBooker(1L, RequestState.REJECTED, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetAllBookingsByOwner() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.countItemsOwnedByUser(1L))
                        .thenReturn(1L);

        Mockito
                .when(bookingRepository.findByItemOwnerIdOrderByStartDesc(1L))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByOwner(1L, RequestState.ALL, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetCurrentBookingsByOwner() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.countItemsOwnedByUser(1L))
                .thenReturn(1L);

        Mockito
                .when(bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByOwner(1L, RequestState.CURRENT, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetPastBookingsByOwner() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.countItemsOwnedByUser(1L))
                .thenReturn(1L);

        Mockito
                .when(bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByOwner(1L, RequestState.PAST, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetFutureBookingsByOwner() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.countItemsOwnedByUser(1L))
                .thenReturn(1L);

        Mockito
                .when(bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByOwner(1L, RequestState.FUTURE, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetWaitingBookingsByOwner() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.countItemsOwnedByUser(1L))
                .thenReturn(1L);

        Mockito
                .when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(1L, BookingStatus.WAITING))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByOwner(1L, RequestState.WAITING, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetRejectedBookingsByOwner() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.countItemsOwnedByUser(1L))
                .thenReturn(1L);

        Mockito
                .when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(1L, BookingStatus.REJECTED))
                .thenReturn(foundBookings);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        List<BookingResponseDto> result = service.getAllBookingsByOwner(1L, RequestState.REJECTED, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetBookingById() {
        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findItemOwnerIdById(1L))
                .thenReturn(1L);

        Mockito
                .when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericBooking));

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        BookingResponseDto result = service.getBookingById(1L, 1L);
        Assertions.assertEquals(genericBookingResponseDto, result);
    }

    @Test
    public void testCreateBooking() {
        Mockito
                .when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericItem));

        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        Mockito
                .when(bookingRepository.save(genericBooking))
                .thenReturn(genericBooking);

        Mockito
                .when(mapper.toBookingResponseDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        BookingResponseDto result = service.createBooking(1L, genericBooking);
        Assertions.assertEquals(genericBookingResponseDto, result);
    }

    @Test
    public void testSetBookingStatusApproved() {
        Mockito
                .when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findItemOwnerIdById(Mockito.anyLong()))
                .thenReturn(2L);

        Mockito
                .when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(genericBooking));

        Mockito
                .when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(genericBooking);

        Mockito
                .when(mapper.toBookingResponseDto(Mockito.any(Booking.class)))
                .thenReturn(new BookingResponseDto(1L, new ItemShortDto(1L, "name"), new UserShortDto(1L, "name"), genericBookingResponseDto.getStart(), genericBookingResponseDto.getEnd(), BookingStatus.APPROVED));

        BookingResponseDto result = service.setBookingStatus(2L, 1L, true);
        genericBookingResponseDto.setStatus(BookingStatus.APPROVED);
        Assertions.assertEquals(genericBookingResponseDto, result);
    }

    @Test
    public void testSetBookingStatusRejected() {
        Mockito
                .when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.findItemOwnerIdById(Mockito.anyLong()))
                .thenReturn(2L);

        Mockito
                .when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(genericBooking));

        Mockito
                .when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(genericBooking);

        Mockito
                .when(mapper.toBookingResponseDto(Mockito.any(Booking.class)))
                .thenReturn(new BookingResponseDto(1L, new ItemShortDto(1L, "name"), new UserShortDto(1L, "name"), genericBookingResponseDto.getStart(), genericBookingResponseDto.getEnd(), BookingStatus.REJECTED));

        BookingResponseDto result = service.setBookingStatus(2L, 1L, false);
        genericBookingResponseDto.setStatus(BookingStatus.REJECTED);
        Assertions.assertEquals(genericBookingResponseDto, result);
    }
}
