package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemResponseDto> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                       @PathVariable Long itemId) {
        return service.getItemById(userId, itemId);
    }

    @PostMapping
    public ItemResponseDto createItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @RequestBody ItemCreateDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return service.createItem(userId, item, itemDto.getRequestId());
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @PathVariable Long itemId,
                                      @RequestBody ItemUpdateDto itemDto) {
        return service.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItemsByText(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                   @RequestParam String text,
                                                   @RequestParam(required = false, defaultValue = "0") Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.searchItemsByText(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                            @PathVariable Long itemId,
                                            @RequestBody CommentCreateDto commentDto) {
        Comment comment = commentMapper.toComment(commentDto);
        return service.createComment(userId, itemId, comment);
    }
}
