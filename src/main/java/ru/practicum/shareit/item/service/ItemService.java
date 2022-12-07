package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingsAndComments;

import java.util.List;

public interface ItemService {

    /**
     * Method returns list of items owned by user.
     *
     * @param userId - ID of user - owner of items.
     * @return - List of items owned by user.
     */
    List<ItemWithBookingsAndComments> getAllItemsByUserId(long userId);

    /**
     * Method returns item by ID.
     *
     * @param userId - ID of user requesting information.
     * @param itemId - ID of item requested.
     * @return - Item with requested ID.
     */
    ItemWithBookingsAndComments getItemById(long userId, long itemId);

    /**
     * Method adds item to repository.
     *
     * @param userId - ID of user adding item.
     * @param item - Item to be added.
     * @return - Added item with assigned ID.
     */
    Item createItem(long userId, Item item);

    /**
     * Method updates item in repository.
     *
     * @param userId - ID of user updating item.
     * @param itemId - ID of item to be updated.
     * @param itemDto - Item to be updated.
     * @return - Updated item with assigned ID.
     */
    Item updateItem(long userId, long itemId, ItemUpdateDto itemDto);

    /**
     * Method returns list of items containing certain text in name or description.
     *
     * @param userId - ID of user searching items.
     * @param text - search text.
     *
     * @return - List of items containing text in name or description.
     */
    List<Item> searchItemsByText(long userId, String text);

    /**
     * Method creates comment to item.
     *
     * @param comment - comment to be created.
     *
     * @return - created comment.
     */
    Comment createComment(CommentEntity comment);
}
