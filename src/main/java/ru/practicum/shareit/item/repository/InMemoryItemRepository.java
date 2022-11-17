package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
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
        long id = getNextId();
        Item newItem = new Item(id,
                item.getName(),
                item.getDescription(),
                userId,
                item.getAvailable());
        items.put(id, newItem);
        return items.get(id);
    }

    @Override
    public Item updateItem(long userId, long itemId, ItemDto item) {
        Item updatableItem = items.get(itemId);
        // TODO: REFACTOR CHECK TO SERVICE
        if (updatableItem.getOwnerId().equals(userId)) {
            if (item.getName() != null) {
                updatableItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                updatableItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                updatableItem.setIsAvailable(item.getAvailable());
            }
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
                .filter(item -> item.getName().contains(text) || item.getDescription().contains(text))
                .collect(Collectors.toList());
    }

    private Long getNextId() {
        return ++globalId;
    }
}
