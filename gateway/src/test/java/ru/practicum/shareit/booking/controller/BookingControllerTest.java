package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class BookingControllerTest {

    private static final UserCreateDto VALID_USER = new UserCreateDto(null, "name", "email@mail.ru");
    private static final UserCreateDto VALID_BOOKER = new UserCreateDto(null, "booker", "booker@mail.ru");
    private static final ItemCreateDto VALID_ITEM = new ItemCreateDto(null, "name", "description", true, null);
    private static final LocalDateTime START = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime END = LocalDateTime.now().plusDays(1).plusHours(1);
    private static final BookingCreateDto VALID_BOOKING = new BookingCreateDto(1L, START, END);
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnGetBookingsWrongState() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "WRONG_STATE")
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnGetBookingsByBookerByWrongPagination() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "ALL")
                                .param("from", "-1")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnGetBookingsByOwnerWrongState() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "WRONG_STATE")
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnGetBookingsByOwnerByWrongPagination() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "ALL")
                                .param("from", "-1")
                                .param("size", "0")
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostBookingWhenStartInPast() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);

        //when
        mockMvc.perform(
                        post("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .content(objectMapper.writeValueAsString(new BookingCreateDto(1L, LocalDateTime.now().minusDays(1), END)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostBookingWhenEndBeforeStart() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);

        //when
        mockMvc.perform(
                        post("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .content(objectMapper.writeValueAsString(new BookingCreateDto(1L, END, START)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    private void postValidUser(UserCreateDto user) throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidItem(ItemCreateDto item) throws Exception {
        mockMvc.perform(
                post("/items")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidBooking(BookingCreateDto booking) throws Exception {
        mockMvc.perform(
                post("/bookings")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }
}
