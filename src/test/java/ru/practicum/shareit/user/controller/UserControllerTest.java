package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerTest {

    private static final UserCreateDto VALID_USER = new UserCreateDto(null, "name", "email@mail.ru");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetUsers() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        get("/users")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].email").value("email@mail.ru"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnGetUserWhenValidId() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        get("/users/1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("email@mail.ru"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn404OnGetUserWhenInvalidId() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        get("/users/99")
                )

                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnPostUserWhenValidUser() throws Exception {
        //when
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(VALID_USER))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("email@mail.ru"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn409OnPostUserWhenDuplicateEmail() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(VALID_USER))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isConflict());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostUserWhenNoName() throws Exception {
        //when
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(new UserCreateDto(null, null, "email@mail.ru")))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostUserWhenNoEmail() throws Exception {
        //when
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(new UserCreateDto(null, "name", null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn400OnPostUserWhenInvalidEmail() throws Exception {
        //when
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(new UserCreateDto(null, "name", "mail.ru")))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnPatchUserWhenValidUser() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        patch("/users/1")
                                .content(objectMapper.writeValueAsString(new UserUpdateDto(null, "name_update", "email_update@mail.ru")))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name_update"))
                .andExpect(jsonPath("$.email").value("email_update@mail.ru"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnPatchUserNameWhenValidUser() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        patch("/users/1")
                                .content(objectMapper.writeValueAsString(new UserUpdateDto(null, "name_update", null)))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name_update"))
                .andExpect(jsonPath("$.email").value("email@mail.ru"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnPatchUserEmailWhenValidUser() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        patch("/users/1")
                                .content(objectMapper.writeValueAsString(new UserUpdateDto(null, null, "email_update@mail.ru")))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("email_update@mail.ru"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn409OnPatchUserWhenDuplicateEmail() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(new UserCreateDto(null, "name 2", "email2@mail.ru"));

        //when
        mockMvc.perform(
                        patch("/users/1")
                                .content(objectMapper.writeValueAsString(new UserUpdateDto(null, null, "email2@mail.ru")))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200OnDeleteUserWhenValidId() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        delete("/users/1")
                )

                //then
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn404OnDeleteUserWhenInvalidId() throws Exception {
        //given
        postValidUser(VALID_USER);

        //when
        mockMvc.perform(
                        delete("/users/99")
                )

                //then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("User with id: 99 is not found",
                        result.getResolvedException().getMessage()));
    }

    private void postValidUser(UserCreateDto user) throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
