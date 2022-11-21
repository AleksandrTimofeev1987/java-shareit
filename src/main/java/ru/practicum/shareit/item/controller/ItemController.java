package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) long userId,
                              @RequestBody ItemDto item) {
        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemDto item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) long userId,
                               @PathVariable long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) long userId,
                                           @RequestParam String text) {
        return itemService.searchItemsByText(userId, text);
    }
}
