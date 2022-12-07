package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemResponseWithBookingsAndCommentsDto> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId) {
        return itemService.getAllItemsByUserId(userId)
                .stream()
                .map(itemMapper::toItemResponseWithBookingsAndCommentsDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemResponseWithBookingsAndCommentsDto getItemById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                              @Min(1L) @PathVariable Long itemId) {
        return itemMapper.toItemResponseWithBookingsAndCommentsDto(itemService.getItemById(userId, itemId));
    }

    @PostMapping
    public ItemResponseDto createItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @Valid @RequestBody ItemCreateDto itemDto) {
        Item item = itemMapper.toItemFromItemCreateDto(itemDto);
        return itemMapper.toItemResponseDto(itemService.createItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @Min(1L) @PathVariable long itemId,
                                      @RequestBody ItemUpdateDto itemDto) {
        return itemMapper.toItemResponseDto(itemService.updateItem(userId, itemId, itemDto));
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItemsByText(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                   @RequestParam String text) {
        return itemService.searchItemsByText(userId, text)
                .stream()
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                            @Min(1L) @PathVariable Long itemId,
                                            @Valid @RequestBody CommentCreateDto commentDto) {
        CommentEntity comment = commentMapper.toCommentCreateFromCommentCreateDto(commentDto, userId, itemId);
        return commentMapper.toCommentResponseDto(itemService.createComment(comment));
    }
}
