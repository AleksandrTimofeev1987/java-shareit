package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient requestClient;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByRequesterId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId) {
        log.debug("Getting all item requests by requester with userId={}", userId);
        return requestClient.getAllItemRequestsByRequesterId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Getting all item requests by others with userId={}", userId);
        validatePaginationParameters(from, size);
        return requestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                     @Positive @PathVariable Long requestId) {
        log.debug("Getting item request by requestId={}", requestId);
        return requestClient.getItemRequestById(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                    @Valid @RequestBody ItemRequestCreateDto itemRequestDto) {
        log.debug("Creating item request by userId={}", userId);
        return requestClient.createItemRequest(userId, itemRequestDto);
    }

    private void validatePaginationParameters(Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new BadRequestException("Incorrect pagination parameters.");
        }
    }
}
