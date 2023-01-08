package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting all items by owner with userId={}", userId);
        validatePaginationParameters(from, size);
        return itemClient.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                       @Positive @PathVariable Long itemId) {
        log.info("Getting item with itemId={}, userId={}", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @RequestBody @Valid ItemCreateDto itemDto) {
        log.debug("Creating item with name={}", itemDto.getName());
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @Positive @PathVariable Long itemId,
                                      @RequestBody ItemUpdateDto itemDto) {
        log.debug("Updating item with id={}", itemId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByText(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                   @RequestParam String text,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Searching all items by text={}, userId={}", text, userId);
        validatePaginationParameters(from, size);
        return itemClient.searchItemsByText(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                            @Positive @PathVariable Long itemId,
                                            @Valid @RequestBody CommentCreateDto commentDto) {
        log.debug("Creating comment for itemId={}", itemId);
        return itemClient.createComment(userId, itemId, commentDto);
    }

    private void validatePaginationParameters(Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new BadRequestException("Incorrect pagination parameters.");
        }
    }
}
