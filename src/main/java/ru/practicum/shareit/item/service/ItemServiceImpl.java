package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validation.SearchValidator;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;

    // TODO: изменить на кастомный метод поиска в репозитории
    @Override
    @Transactional(readOnly = true)
    public List<Item> getAllItemsByUserId(long userId) {
        log.debug("A list of all items owned by user with ID - {} is requested.", userId);
        validateUserExists(userId);
        List<Item> items = itemRepository.findAll();
        log.debug("A list of all items owned by user with ID - {} is received with size of {}.", userId, items.size());
        return items.stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(long userId, long itemId) {
        log.debug("Item with ID - {} is requested.", itemId);
        validateUserExists(userId);
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        validateItemExists(itemId, itemOpt);

        log.debug("Item with ID - {} is received from repository.", userId);
        return itemOpt.get();
    }

    @Override
    public Item createItem(long userId, Item item) {
        log.debug("Request to add item with name - {} is received.", item.getName());
        validateUserExists(userId);
        item.setOwnerId(userId);
        Item addedItem = itemRepository.save(item);
        log.debug("Item with ID - {} is added to repository.", item.getId());
        return addedItem;
    }

    @Override
    public Item updateItem(long userId, long itemId, ItemUpdateDto itemDto) {
        log.debug("Request to update item with ID - {} is received.", itemId);
        validateUserExists(userId);
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        validateItemExists(itemId, itemOpt);
        Item itemForUpgrade = itemOpt.get();

        validateUserOwnItem(userId, itemForUpgrade);

        mapper.updateItemFromItemUpdate(itemDto, itemForUpgrade);

        Item updatedItem = itemRepository.save(itemForUpgrade);
        log.debug("Item with ID - {} is updated in repository.", itemId);
        return updatedItem;
    }

    @Override
    public List<Item> searchItemsByText(long userId, String text) {
        log.debug("A list of all items containing text ({}) in name or description is requested.", text);
        String lowerCaseText = text.toLowerCase();
        validateUserExists(userId);
        if (!SearchValidator.validateText(text)) {
            return new ArrayList<>();
        }

        List<Item> items = itemRepository.findAll();

        log.debug("A list of all items containing text ({}) in name or description is received with size of {}.", text, items.size());
        return items.stream()
                .filter(item -> item.getName().toLowerCase().contains(lowerCaseText) || item.getDescription().toLowerCase().contains(lowerCaseText))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id: %d is not found", userId));
        }
    }

    private void validateItemExists(long itemId, Optional<Item> itemOpt) {
        if (!itemOpt.isPresent()) {
            throw new NotFoundException(String.format("Item with id: %d is not found", itemId));
        }
    }

    private void validateUserOwnItem(long userId, Item itemForUpgrade) {
        if (!itemForUpgrade.getOwnerId().equals(userId)) {
            throw new ForbiddenException(String.format("User with id: %d does not own item with id: %d", userId, itemForUpgrade.getId()));
        }
    }
}
