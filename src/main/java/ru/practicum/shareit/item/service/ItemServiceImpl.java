package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validation.SearchValidator;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public List<ItemResponseDto> getAllItemsByUserId(Long userId) {
        log.debug("A list of all items owned by user with ID - {} is requested.", userId);

        validateUserExists(userId);
        List<Item> items = itemRepository.findByOwnerId(userId);

        log.debug("A list of all items owned by user with ID - {} is received with size of {}.", userId, items.size());
        return items
                .stream()
                .map(item -> toItemResponseDto(userId, item))
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(Long userId, Long itemId) {
        log.debug("Item with ID - {} is requested.", itemId);

        validateUserExists(userId);
        Item savedItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId)));

        ItemResponseDto result = toItemResponseDto(userId, savedItem);

        log.debug("Item with ID - {} is received.", result.getId());
        return result;
    }

    @Override
    public Item createItem(Long userId, Item item) {
        log.debug("Request to add item with name - {} is received.", item.getName());

        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));
        Item savedItem = itemRepository.save(item);

        log.debug("Item with id - {} is created.", item.getId());
        return savedItem;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, ItemUpdateDto itemDto) {
        log.debug("Request to update item with ID - {} is received.", itemId);

        validateUserExists(userId);
        validateItemExists(itemId);

        Item itemForUpdate = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId)));
        validateUserOwnItem(userId, itemForUpdate);

        itemMapper.toItemFromItemUpdateDto(itemDto, itemForUpdate);

        Item updatedItem = itemRepository.save(itemForUpdate);

        log.debug("Item with ID - {} is updated in repository.", updatedItem.getId());
        return updatedItem;
    }

    @Override
    public List<Item> searchItemsByText(Long userId, String text) {
        log.debug("A list of all items containing text ({}) in name or description is requested.", text);

        validateUserExists(userId);
        List<Item> result = new ArrayList<>();
        if (!SearchValidator.validateText(text)) {
            return result;
        }

        result = itemRepository.searchByNameOrDescription(text);

        log.debug("A list of all items containing text ({}) in name or description is received with size of {}.", text, result.size());
        return result;
    }

    @Override
    public Comment createComment(Long userId, Long itemId, Comment comment) {
        log.debug("Request to add comment to item {} by user with id {} received.", itemId, userId);

        validateUserExists(userId);
        validateItemExists(itemId);
        validateUserBookedItemAndBookingEnded(userId, itemId);

        comment.setItem(itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", userId))));
        comment.setAuthor(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));

        Comment savedComment = commentRepository.save(comment);

        log.debug("Comment to item {} is created with id {}.", itemId, savedComment.getId());
        return savedComment;
    }

    private ItemResponseDto toItemResponseDto(Long userId, Item item) {
        if (item == null) {
            return null;
        }

        ItemResponseDto responseDto = new ItemResponseDto();

        responseDto.setId(item.getId());
        responseDto.setName(item.getName());
        responseDto.setDescription(item.getDescription());
        responseDto.setOwner(userMapper.toUserResponseDto(item.getOwner()));
        responseDto.setAvailable(item.getAvailable());

        populateItemDtoWithBookings(userId, item, responseDto);

        populateItemDtoWithComments(item, responseDto);

        return responseDto;
    }

    private void populateItemDtoWithBookings(Long userId, Item item, ItemResponseDto responseDto) {
        if (checkUserIsOwner(userId, item)) {
            LocalDateTime now = LocalDateTime.now();
            Booking lastBooking = bookingRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc(item.getId(), now);
            Booking nextBooking = bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), now);
            responseDto.setLastBooking(bookingMapper.toBookingShortDto(lastBooking));
            responseDto.setNextBooking(bookingMapper.toBookingShortDto(nextBooking));
        }
    }

    private void populateItemDtoWithComments(Item item, ItemResponseDto responseDto) {
        Set<Comment> comments = commentRepository.findByItemId(item.getId());
        responseDto.setComments(comments
                .stream()
                .map(commentMapper::toCommentResponseDto)
                .collect(Collectors.toSet()));
    }

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id: %d is not found", userId));
        }
    }

    private void validateItemExists(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(String.format("Item with id: %d is not found", itemId));
        }
    }

    private void validateUserOwnItem(Long userId, Item itemForUpdate) {
        if (!userId.equals(itemForUpdate.getOwner().getId())) {
            throw new ForbiddenException(String.format("User with id: %d does not own item with id: %d", userId, itemForUpdate.getId()));
        }
    }

    private void validateUserBookedItemAndBookingEnded(Long authorId, Long itemId) {
        if (bookingRepository.countByBookerIdItemIdAndPast(authorId, itemId, LocalDateTime.now()) < 1) {
            throw new BadRequestException("Comment cannot be published for unfinished or future booking");
        }
    }

    private boolean checkUserIsOwner(Long userId, Item item) {
        return item.getOwner().getId().equals(userId);
    }
}
