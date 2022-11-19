package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.model.item.ItemDoesNotExistException;
import ru.practicum.shareit.exception.model.item.ItemIsNotOwnedByUserException;
import ru.practicum.shareit.exception.model.user.UserDoesNotExistException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("InMemoryItemRepository")
public class InMemoryItemRepository implements ItemRepository {

    private Map<Long, Item> items = new HashMap<>();
    private Long globalId = 0L;

    @Override
    public Item createItem(long userId, ItemDto item) {
        long itemId = getNextId();
        Item newItem = ItemMapper.mapDtoToItem(item);
        newItem.setId(itemId);
        newItem.setOwnerId(userId);
        items.put(itemId, newItem);
        return items.get(itemId);
    }

    @Override
    public Item updateItem(long userId, long itemId, ItemDto item) {
        Item updatableItem = items.get(itemId);
        if (item.getName() != null) {
            updatableItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatableItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatableItem.setIsAvailable(item.getAvailable());
        }
        return items.get(itemId);
    }

    @Override
    public Item getItemById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItemsByText(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text) && item.getIsAvailable().equals(true))
                .collect(Collectors.toList());
    }

    @Override
    public void validateItemExists(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new ItemDoesNotExistException(String.format("Item with id: %d is not found", itemId));
        }
    }

    @Override
    public void validateUserOwnItem(long userId, long itemId) {
        if(!items.get(itemId).getOwnerId().equals(userId)) {
            throw new ItemIsNotOwnedByUserException(String.format("Item requested to be updated with ID - %d is not owned by user with ID - %d", itemId, userId));
        }
    }

    private Long getNextId() {
        return ++globalId;
    }
}
