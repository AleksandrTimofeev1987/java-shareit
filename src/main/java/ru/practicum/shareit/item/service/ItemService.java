package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.model.ItemWithBookingsAndComments;

import java.util.List;

public interface ItemService {

    /**
     * Method returns list of items owned by user.
     *
     * @param userId - ID of user - owner of items.
     * @return - List of items owned by user.
     */
    List<ItemWithBookingsAndComments> getAllItemsByUserId(Long userId);

    /**
     * Method returns item by ID.
     *
     * @param userId - ID of user requesting information.
     * @param itemId - ID of item requested.
     * @return - Item with requested ID.
     */
    ItemWithBookingsAndComments getItemById(Long userId, Long itemId);

    /**
     * Method adds item to repository.
     *
     * @param userId - ID of user adding item.
     * @param item - Item to be added.
     * @return - Added item with assigned ID.
     */
    ItemEntity createItem(Long userId, ItemEntity item);

    /**
     * Method updates item in repository.
     *
     * @param userId - ID of user updating item.
     * @param itemId - ID of item to be updated.
     * @param itemDto - Item to be updated.
     * @return - Updated item with assigned ID.
     */
    ItemEntity updateItem(Long userId, Long itemId, ItemUpdateDto itemDto);

    /**
     * Method returns list of items containing certain text in name or description.
     *
     * @param userId - ID of user searching items.
     * @param text - search text.
     *
     * @return - List of items containing text in name or description.
     */
    List<ItemEntity> searchItemsByText(Long userId, String text);

    /**
     * Method creates comment to item.
     *
     * @param comment - comment to be created.
     *
     * @return - created comment.
     */
    Comment createComment(CommentEntity comment);
}
