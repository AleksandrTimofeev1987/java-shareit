package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.item.validation.SearchValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;

    @Override
    public ItemResponse createItem(long userId, ItemCreateRequest item) {
        log.debug("Request to add item with name - {} is received.", item.getName());
        userRepository.validateUserExists(userId);

        Item addedItem = itemRepository.createItem(userId, item);
        log.debug("Item with ID - {} is added to repository.", item.getId());
        return mapper.toItemResponse(addedItem);
    }

    @Override
    public ItemResponse updateItem(long userId, long itemId, UpdateItemDto item) {
        log.debug("Request to update item with ID - {} is received.", itemId);
        userRepository.validateUserExists(userId);
        itemRepository.validateUserOwnItem(userId, itemId);

        Item updatedItem = itemRepository.updateItem(userId, itemId, item);
        log.debug("Item with ID - {} is updated in repository.", itemId);
        return mapper.toItemResponse(updatedItem);
    }

    @Override
    public ItemResponse getItemById(long userId, long itemId) {
        log.debug("Item with ID - {} is requested.", itemId);
        userRepository.validateUserExists(userId);
        itemRepository.validateItemExists(itemId);

        Item item = itemRepository.getItemById(itemId);
        log.debug("Item with ID - {} is received from repository.", userId);
        return mapper.toItemResponse(item);
    }

    @Override
    public List<ItemResponse> getAllItemsByUserId(long userId) {
        log.debug("A list of all items owned by user with ID - {} is requested.", userId);
        userRepository.validateUserExists(userId);

        List<Item> items = itemRepository.getAllItemsByUserId(userId);
        log.debug("A list of all items owned by user with ID - {} is received with size of {}.", userId, items.size());
        return items.stream()
                .map(mapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> searchItemsByText(long userId, String text) {
        log.debug("A list of all items containing text ({}) in name or description is requested.", text);
        userRepository.validateUserExists(userId);
        if (!SearchValidator.validateText(text)) {
            return new ArrayList<>();
        }

        List<Item> items = itemRepository.searchItemsByText(text.toLowerCase());
        log.debug("A list of all items containing text ({}) in name or description is received with size of {}.", text, items.size());
        return items.stream()
                .map(mapper::toItemResponse)
                .collect(Collectors.toList());
    }
}
