package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ItemControllerTest {

    private static final UserCreateDto VALID_USER = new UserCreateDto(null, "name", "email@mail.ru");
    private static final UserCreateDto VALID_BOOKER = new UserCreateDto(null, "booker", "booker@mail.ru");
    private static final ItemCreateDto VALID_ITEM = new ItemCreateDto(null, "name", "description", true, null);
    private static final CommentCreateDto VALID_COMMENT = new CommentCreateDto(null, "text");
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";
    private static final LocalDateTime START = LocalDateTime.now().plusMinutes(10);
    private static final LocalDateTime END = LocalDateTime.now().plusMinutes(11);
    private static final BookingCreateDto VALID_BOOKING = new BookingCreateDto(1L, START, END);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostItemWhenNoHeader() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/items")
                                .content(objectMapper.writeValueAsString(VALID_ITEM))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostItemWhenMissingAvailable() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemCreateDto(null, "name", "description", null, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostItemWhenEmptyName() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemCreateDto(null, "", "description", true, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostItemWhenNoName() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemCreateDto(null, null, "description", true, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostItemWhenEmptyDescription() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemCreateDto(null, "name", "", true, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostItemWhenNoDescription() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/items")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(new ItemCreateDto(null, "name", null, true, null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPatchItemWhenNoHeader() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        patch("/items/1")
                                .content(objectMapper.writeValueAsString(new ItemUpdateDto(1L, "name_updated", "description_updated", false)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnSearchItemsByWrongPagination() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("text", "eScri")
                                .param("from", "-1")
                                .param("size", "0")
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200AndEmptyListOnSearchItemsByBlankText() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("text", "")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0));
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
}
