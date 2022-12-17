package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService service;
    private final ItemRequestMapper mapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemRequestResponseDto> getAllItemRequestsByRequesterId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId) {
        return service.getAllItemRequestsByRequesterId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getItemRequestById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                     @Min(1L) @PathVariable Long requestId) {
        return service.getItemRequestById(userId, requestId);
    }

    @PostMapping
    public ItemRequestResponseDto createItemRequest(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE)Long userId,
                                                    @Valid @RequestBody ItemRequestCreateDto itemRequestDto) {
        ItemRequest itemRequest = mapper.toItemRequest(itemRequestDto);
        return service.createItemRequest(userId, itemRequest);
    }

}
