package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository("InMemoryItemRepository")
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {

    private Map<Long, Item> items = new HashMap<>();
    private Long globalId = 0L;
    private final ItemMapper mapper;

    @Override
    public Item createItem(long userId, ItemCreateRequest item) {
        long itemId = getNextId();
        Item newItem = mapper.toItem(item);
        newItem.setId(itemId);
        newItem.setOwnerId(userId);
        items.put(itemId, newItem);
        return items.get(itemId);
    }

    @Override
    public Item updateItem(long userId, long itemId, UpdateItemDto item) {
        Item storedItem = items.get(itemId);
        if (Objects.isNull(storedItem)) {
            return null;
        }

        if (item.getName() != null && !Objects.equals(storedItem.getName(), item.getName())) {
            storedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !Objects.equals(storedItem.getDescription(), item.getDescription())) {
            storedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null && !Objects.equals(storedItem.getAvailable(), item.getAvailable())) {
            storedItem.setAvailable(item.getAvailable());
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
                .filter(item -> item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    @Override
    public void validateItemExists(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException(String.format("Item with id: %d is not found", itemId));
        }
    }

    @Override
    public void validateUserOwnItem(long userId, long itemId) {
        if (!items.get(itemId).getOwnerId().equals(userId)) {
            throw new ForbiddenException(String.format("Item requested to be updated with ID - %d is not owned by user with ID - %d", itemId, userId));
        }
    }

    private Long getNextId() {
        return ++globalId;
    }
}
