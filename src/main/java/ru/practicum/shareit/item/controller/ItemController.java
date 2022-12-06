package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
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
    private final ItemMapper mapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemResponseDto> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId) {
        return itemService.getAllItemsByUserId(userId)
                .stream()
                .map(mapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                       @Min(1L) @PathVariable Long itemId) {
        return mapper.toItemResponseDto(itemService.getItemById(userId, itemId));
    }

    @PostMapping
    public ItemResponseDto createItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @Valid @RequestBody ItemCreateDto itemDto) {
        Item item = mapper.toItemFromItemCreateDto(itemDto);
        return mapper.toItemResponseDto(itemService.createItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                      @Min(1L) @PathVariable long itemId,
                                      @RequestBody ItemUpdateDto itemDto) {
        return mapper.toItemResponseDto(itemService.updateItem(userId, itemId, itemDto));
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItemsByText(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                   @RequestParam String text) {
        return itemService.searchItemsByText(userId, text)
                .stream()
                .map(mapper::toItemResponseDto)
                .collect(Collectors.toList());
    }
}
