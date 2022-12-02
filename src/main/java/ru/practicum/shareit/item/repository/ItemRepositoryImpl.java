package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @Override
    public Item createItem(long userId, ItemCreateRequest item) {
        return null;
    }

    @Override
    public Item updateItem(long userId, long itemId, UpdateItemDto item) {
        return null;
    }

    @Override
    public Item getItemById(long itemId) {
        return null;
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return null;
    }

    @Override
    public List<Item> searchItemsByText(String text) {
        return null;
    }

    @Override
    public void validateItemExists(long itemId) {

    }

    @Override
    public void validateUserOwnItem(long userId, long itemId) {

    }
}
