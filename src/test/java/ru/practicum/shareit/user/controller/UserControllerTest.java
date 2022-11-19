package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private static final String NAME = "name";
    private static final String UPDATED_NAME = "updated_name";
    private static final String EMAIL = "new@yandex.ru";
    private static final String UPDATED_EMAIL = "updated@yandex.ru";
    private static final UserDto USER = new UserDto(null, NAME,EMAIL);

    // Testing getting all users
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetAllWhenValidUser() throws Exception {
        //given
        postValidUser();

        //when
        mockMvc.perform(
                        get("/users")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL));
    }

    // Testing getting user by ID
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200AndUserOnGetUserWhenValidUserId() throws Exception {
        //given
        postValidUser();

        //when
        mockMvc.perform(
                        get("/users/{id}", 1L)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    // Testing adding new user
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnPostUserWhenValidUser() throws Exception {
        //when
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(USER))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    // Testing full update of user
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnPatchUserWhenValidUser() throws Exception {
        //given
        postValidUser();

        UserDto updatedUser = new UserDto(null, UPDATED_NAME, UPDATED_EMAIL);

        //when
        mockMvc.perform(
                        patch("/users/{id}", 1L)
                                .content(objectMapper.writeValueAsString(updatedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.email").value(UPDATED_EMAIL));
    }

    // Testing update of username
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnPatchUserNameWhenValidUser() throws Exception {
        //given
        postValidUser();

        UserDto updatedUser = new UserDto(null, UPDATED_NAME, null);

        //when
        mockMvc.perform(
                        patch("/users/{id}", 1L)
                                .content(objectMapper.writeValueAsString(updatedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    // Testing update of user email
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andUserOnPatchUserEmailWhenValidUser() throws Exception {
        //given
        postValidUser();

        UserDto updatedUser = new UserDto(null, null, UPDATED_EMAIL);

        //when
        mockMvc.perform(
                        patch("/users/{id}", 1L)
                                .content(objectMapper.writeValueAsString(updatedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(UPDATED_EMAIL));
    }

    // Testing delete of user by ID
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200AndUserOnDeleteUserWhenValidUserId() throws Exception {
        //given
        postValidUser();

        //when
        mockMvc.perform(
                        delete("/users/{id}", 1L)
                )

                //then
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/users")
                )
                .andExpect(jsonPath("length()").value(0));
    }

    private void postValidUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(USER))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
