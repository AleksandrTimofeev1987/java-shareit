package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.item.validation.ItemValidator;
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

    @Override
    public ItemDto createItem(long userId, ItemDto item) {
        log.debug("Request to add item with name - {} is received from user with ID - {}.", item.getName(), userId);
        userRepository.validateUserExists(userId);
        ItemValidator.validateItem(item);

        Item addedItem = itemRepository.createItem(userId, item);
        log.debug("Item with ID - {} is added to repository.", item.getId());
        return ItemMapper.mapItemToDto(addedItem);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto item) {
        log.debug("Request to update item with ID - {} is received from user with ID - {}.", itemId, userId);
        userRepository.validateUserExists(userId);
        itemRepository.validateUserOwnItem(userId, itemId);

        Item updatedItem = itemRepository.updateItem(userId, itemId, item);
        log.debug("Item with ID - {} is updated in repository.", itemId);
        return ItemMapper.mapItemToDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(long userId, long itemId) {
        log.debug("Item with ID - {} is requested from user with ID - {}.", itemId, userId);
        userRepository.validateUserExists(userId);
        itemRepository.validateItemExists(itemId);

        Item item = itemRepository.getItemById(itemId);
        log.debug("Item with ID - {} is received from repository.", userId);
        return ItemMapper.mapItemToDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(long userId) {
        log.debug("A list of all items owned by user with ID - {} is requested.", userId);
        userRepository.validateUserExists(userId);

        List<Item> items = itemRepository.getAllItemsByUserId(userId);
        log.debug("A list of all items owned by user with ID - {} is received with size of {}.", userId, items.size());
        return items.stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItemsByText(long userId, String text) {
        log.debug("A list of all items containing text ({}) in name or description is requested by user with ID - {}.", text, userId);
        userRepository.validateUserExists(userId);
        if (!SearchValidator.validateText(text)) {
            return new ArrayList<>();
        }

        List<Item> items = itemRepository.searchItemsByText(text.toLowerCase());
        log.debug("A list of all items containing text ({}) in name or description is received with size of {}.", text, items.size());
        return items.stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }
}
