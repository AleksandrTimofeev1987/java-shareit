package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    /**
     * Method adds item to repository.
     *
     * @param userId - ID of user adding item.
     * @param item - Item (DTO) to be added.
     * @return - Added item (DTO) with assigned ID.
     */
    ItemResponse createItem(long userId, ItemCreateRequest item);

    /**
     * Method updates item in repository.
     *
     * @param userId - ID of user updating item.
     * @param itemId - ID of item to be updated.
     * @param item - Item (DTO) to be updated.
     * @return - Updated item (DTO) with assigned ID.
     */
    ItemResponse updateItem(long userId, long itemId, UpdateItemDto item);

    /**
     * Method returns item (DTO) by ID.
     *
     * @param userId - ID of user requesting information.
     * @param itemId - ID of item requested.
     * @return - Item (DTO) with requested ID.
     */
    ItemResponse getItemById(long userId, long itemId);

    /**
     * Method returns list of items (DTO) owned by user.
     *
     * @param userId - ID of user - owner of items.
     * @return - List of items (DTO) owned by user.
     */
    List<ItemResponse> getAllItemsByUserId(long userId);

    /**
     * Method returns list of items (DTO) containing certain text in name or description.
     *
     * @param userId - ID of user searching items.
     * @param text - search text.
     *
     * @return - List of items (DTO) containing text in name or description.
     */
    List<ItemResponse> searchItemsByText(long userId, String text);
}
