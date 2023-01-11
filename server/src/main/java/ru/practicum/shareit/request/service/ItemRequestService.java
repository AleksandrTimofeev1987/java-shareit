package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    /**
     * Method returns list of item requests created by user.
     *
     * @param userId ID of user - creator of item requests.
     *
     * @return List of item requests created by user.
     */
    List<ItemRequestResponseDto> getAllItemRequestsByRequesterId(Long userId);

    /**
     * Method returns list of item requests created by other users.
     *
     * @param userId ID of user requesting information.
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return List of item requests created by other users.
     */
    List<ItemRequestResponseDto> getAllItemRequests(Long userId, Integer from, Integer size);

    /**
     * Method item request by ID.
     *
     * @param userId ID of user requesting information.
     * @param requestId Index of first element in the sample.
     *
     * @return Item requests with requested ID.
     */
    ItemRequestResponseDto getItemRequestById(Long userId, Long requestId);

    /**
     * Method adds item request to repository.
     *
     * @param userId ID of user adding item request.
     * @param itemRequest Item request to be added.
     *
     * @return Added item request with assigned ID.
     */
    ItemRequestResponseDto createItemRequest(Long userId, ItemRequest itemRequest);
}
