package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private static final String NAME = "name";
    private static final String EMAIL = "new@yandex.ru";
    private static final String ITEM_NAME = "item_name";
    private static final String UPDATED_ITEM_NAME = "updated_item_name";
    private static final String ITEM_DESCRIPTION = "item_description";
    private static final String UPDATED_ITEM_DESCRIPTION = "updated_item_description";
    private static final Boolean AVAILABLE = true;
    private static final Boolean NOT_AVAILABLE = false;

    private static final UserDto USER = new UserDto(null, NAME, EMAIL);
    private static final ItemDto ITEM = new ItemDto(null, ITEM_NAME, ITEM_DESCRIPTION, null, AVAILABLE);

    // Testing adding new item
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPostItemWhenValidItem() throws Exception {
        //given
        postValidUser();

        //when
        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", 1L)
                                .content(objectMapper.writeValueAsString(ITEM))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(ITEM_NAME))
                .andExpect(jsonPath("$.description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.available").value(AVAILABLE));
    }

    // Testing full update of item
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPatchItemWhenValidItem() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto updatedItem = new ItemDto(null, UPDATED_ITEM_NAME, UPDATED_ITEM_DESCRIPTION, null, NOT_AVAILABLE);

        //when
        mockMvc.perform(
                        patch("/items/{itemId}", 1L)
                                .header("X-Sharer-User-Id", 1L)
                                .content(objectMapper.writeValueAsString(updatedItem))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(UPDATED_ITEM_NAME))
                .andExpect(jsonPath("$.description").value(UPDATED_ITEM_DESCRIPTION))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.available").value(NOT_AVAILABLE));
    }

    // Testing update of item name
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPatchItemNameWhenValidItem() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto updatedItem = new ItemDto(null, UPDATED_ITEM_NAME, null, null, null);

        //when
        mockMvc.perform(
                        patch("/items/{itemId}", 1L)
                                .header("X-Sharer-User-Id", 1L)
                                .content(objectMapper.writeValueAsString(updatedItem))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(UPDATED_ITEM_NAME))
                .andExpect(jsonPath("$.description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.available").value(AVAILABLE));
    }

    // Testing update of item description
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPatchItemDescriptionWhenValidItem() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto updatedItem = new ItemDto(null, null, UPDATED_ITEM_DESCRIPTION, null, null);

        //when
        mockMvc.perform(
                        patch("/items/{itemId}", 1L)
                                .header("X-Sharer-User-Id", 1L)
                                .content(objectMapper.writeValueAsString(updatedItem))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(ITEM_NAME))
                .andExpect(jsonPath("$.description").value(UPDATED_ITEM_DESCRIPTION))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.available").value(AVAILABLE));
    }

    // Testing update of item availability
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andItemOnPatchItemAvailableWhenValidItem() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto updatedItem = new ItemDto(null, null, null, null, NOT_AVAILABLE);

        //when
        mockMvc.perform(
                        patch("/items/{itemId}", 1L)
                                .header("X-Sharer-User-Id", 1L)
                                .content(objectMapper.writeValueAsString(updatedItem))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(ITEM_NAME))
                .andExpect(jsonPath("$.description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.available").value(NOT_AVAILABLE));
    }

    // Testing getting item by ID
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200AndItemOnGetItemWhenValidItemId() throws Exception {
        //given
        postValidUser();
        postValidItem();

        //when
        mockMvc.perform(
                        get("/items/{itemId}", 1L)
                                .header("X-Sharer-User-Id", 1L)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(ITEM_NAME))
                .andExpect(jsonPath("$.description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.available").value(AVAILABLE));
    }

    // Testing getting all items owned by user
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetAllWhenValidItem() throws Exception {
        //given
        postValidUser();
        postValidItem();

        //when
        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", 1L)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(ITEM_NAME))
                .andExpect(jsonPath("$[0].description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$[0].ownerId").value(1))
                .andExpect(jsonPath("$[0].available").value(AVAILABLE));
    }

    // Testing searching all items containing text in name or description when text = ""
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andEmptyListOnGetSearchWhenNoTextAllAvailable() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto otherItem = new ItemDto(null, "Drill", "Drilling extravaganza", null, AVAILABLE);
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(otherItem))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header("X-Sharer-User-Id", 1L)
                                .param("text", "")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0));
    }

    // Testing searching all items containing text in name or description when text = name and all items available
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetSearchWhenTextNameAllAvailableOneMatching() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto otherItem = new ItemDto(null, "Drill", "Drilling extravaganza", null, AVAILABLE);
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(otherItem))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header("X-Sharer-User-Id", 1L)
                                .param("text", "nAmE")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(ITEM_NAME))
                .andExpect(jsonPath("$[0].description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$[0].ownerId").value(1))
                .andExpect(jsonPath("$[0].available").value(AVAILABLE));
    }

    // Testing searching all items containing text in name or description when text = name and all items available
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetSearchWhenTextNameAllAvailable() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto otherItem = new ItemDto(null, "Drill name", "Drilling extravaganza", null, AVAILABLE);
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(otherItem))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header("X-Sharer-User-Id", 1L)
                                .param("text", "nAmE")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(ITEM_NAME))
                .andExpect(jsonPath("$[0].description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$[0].ownerId").value(1))
                .andExpect(jsonPath("$[0].available").value(AVAILABLE))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Drill name"))
                .andExpect(jsonPath("$[1].description").value("Drilling extravaganza"))
                .andExpect(jsonPath("$[1].ownerId").value(1))
                .andExpect(jsonPath("$[1].available").value(AVAILABLE));
    }

    // Testing searching all items containing text in name or description when text = name and one item available
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetSearchWhenTextNameOneAvailable() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto otherItem = new ItemDto(null, "Drill name", "Drilling extravaganza", null, false);
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(otherItem))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header("X-Sharer-User-Id", 1L)
                                .param("text", "nAmE")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(ITEM_NAME))
                .andExpect(jsonPath("$[0].description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$[0].ownerId").value(1))
                .andExpect(jsonPath("$[0].available").value(AVAILABLE));
    }

    // Testing searching all items containing text in name or description when text = description and all items available
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetSearchWhenTextDescriptionAllAvailableOneMatching() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto otherItem = new ItemDto(null, "Drill", "Drilling extravaganza", null, AVAILABLE);
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(otherItem))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header("X-Sharer-User-Id", 1L)
                                .param("text", "dEscRiptioN")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(ITEM_NAME))
                .andExpect(jsonPath("$[0].description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$[0].ownerId").value(1))
                .andExpect(jsonPath("$[0].available").value(AVAILABLE));
    }

    // Testing searching all items containing text in name or description when text = description and all items available
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetSearchWhenTextDescriptionAllAvailable() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto otherItem = new ItemDto(null, "Drill", "Drilling extravaganza description", null, AVAILABLE);
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(otherItem))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header("X-Sharer-User-Id", 1L)
                                .param("text", "dEscRiptioN")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(ITEM_NAME))
                .andExpect(jsonPath("$[0].description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$[0].ownerId").value(1))
                .andExpect(jsonPath("$[0].available").value(AVAILABLE))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Drill"))
                .andExpect(jsonPath("$[1].description").value("Drilling extravaganza description"))
                .andExpect(jsonPath("$[1].ownerId").value(1))
                .andExpect(jsonPath("$[1].available").value(AVAILABLE));
    }

    // Testing searching all items containing text in name or description when text = description and one item available
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetSearchWhenTextDescriptionOneAvailable() throws Exception {
        //given
        postValidUser();
        postValidItem();

        ItemDto otherItem = new ItemDto(null, "Drill", "Drilling extravaganza description", null, false);
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(otherItem))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //when
        mockMvc.perform(
                        get("/items/search")
                                .header("X-Sharer-User-Id", 1L)
                                .param("text", "DescRiPtion")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(ITEM_NAME))
                .andExpect(jsonPath("$[0].description").value(ITEM_DESCRIPTION))
                .andExpect(jsonPath("$[0].ownerId").value(1))
                .andExpect(jsonPath("$[0].available").value(AVAILABLE));
    }

    private void postValidUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(USER))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidItem() throws Exception {
        mockMvc.perform(
                post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(ITEM))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
