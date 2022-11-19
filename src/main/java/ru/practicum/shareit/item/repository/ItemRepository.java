package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    /**
     * Method adds item to repository.
     *
     * @param userId - ID of user adding item.
     * @param item - Item (DTO) to be added.
     * @return - Added item with assigned ID.
     */
    Item createItem(long userId, ItemDto item);

    /**
     * Method updates item in repository.
     *
     * @param userId - ID of user updating item.
     * @param itemId - ID of item to be updated.
     * @param item - Item (DTO) to be updated.
     * @return - Updated item with assigned ID.
     */
    Item updateItem(long userId, long itemId, ItemDto item);

    /**
     * Method returns item by ID.
     *
     * @param itemId - ID of item requested.
     * @return - Item with requested ID.
     */
    Item getItemById(long itemId);

    /**
     * Method returns list of items owned by user.
     *
     * @param userId - ID of user - owner of items.
     * @return - List of items owned by user.
     */
    List<Item> getAllItemsByUserId(long userId);

    /**
     * Method returns list of items containing certain text in name or description.
     *
     * @param text - search text.
     *
     * @return - List of items (DTO) containing text in name or description.
     */
    List<Item> searchItemsByText(String text);

    /**
     * Method checks if item exists by id.
     *
     * @param itemId - Item ID to be validated.
     */
    void validateItemExists(long itemId);

    /**
     * Method checks if item with itemId is owned by user with userId.
     *
     * @param userId - ID of user to be validated.
     * @param itemId - ID of item to be validated.
     */
    void validateUserOwnItem(long userId, long itemId);
}
