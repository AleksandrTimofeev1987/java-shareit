package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemService {

    /**
     * Method returns list of items owned by user.
     *
     * @param userId ID of user - owner of items.
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return List of items owned by user.
     */
    List<ItemResponseDto> getAllItemsByUserId(Long userId, Integer from, Integer size);

    /**
     * Method returns item by ID.
     *
     * @param userId ID of user requesting information.
     * @param itemId ID of item requested.
     *
     * @return Item with requested ID.
     */
    ItemResponseDto getItemById(Long userId, Long itemId);

    /**
     * Method adds item to repository.
     *
     * @param userId ID of user adding item.
     * @param item Item to be added.
     * @param requestId Item request id.
     * @return - Added item with assigned ID.
     */
    ItemResponseDto createItem(Long userId, Item item, Long requestId);

    /**
     * Method updates item in repository.
     *
     * @param userId ID of user updating item.
     * @param itemId ID of item to be updated.
     * @param itemDto Item to be updated.
     *
     * @return Updated item with assigned ID.
     */
    ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto itemDto);

    /**
     * Method returns list of items containing certain text in name or description.
     *
     * @param userId ID of user searching items.
     * @param text Search text.
     * @param from Index of first element in the sample.
     * @param size Size of elements shown on one page.
     *
     * @return List of items containing text in name or description.
     */
    List<ItemResponseDto> searchItemsByText(Long userId, String text, Integer from, Integer size);


    /**
     * Method creates comment to item.
     *
     * @param userId User creating comment.
     * @param itemId Item to be commented.
     * @param comment Comment to be created.
     *
     * @return Created comment.
     */
    CommentResponseDto createComment(Long userId, Long itemId, Comment comment);
}
