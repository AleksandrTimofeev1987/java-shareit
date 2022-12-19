package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validation.SearchValidator;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
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

    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItemsByUserId(Long userId, Integer from, Integer size) {
        log.debug("A list of all items owned by user with ID - {} is requested.", userId);

        validateUserExists(userId);

        List<Item> foundItems;
        if (from == null || size == null) {
            foundItems = itemRepository.findByOwnerIdOrderById(userId);
        } else {
            validatePaginationParameters(from, size);
            Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);
            foundItems = itemRepository.findByOwnerId(userId, page);
        }

        log.debug("A list of all items owned by user with ID - {} is received with size of {}.", userId, foundItems.size());
        return foundItems
                .stream()
                .map(item -> toItemResponseDto(userId, item))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(Long userId, Long itemId) {
        log.debug("Item with ID - {} is requested.", itemId);

        validateUserExists(userId);
        Item foundItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId)));

        ItemResponseDto result = toItemResponseDto(userId, foundItem);

        log.debug("Item with ID - {} is received.", result.getId());
        return result;
    }

    @Override
    @Transactional
    public ItemResponseDto createItem(Long userId, Item item, Long requestId) {
        log.debug("Request to add item with name - {} is received.", item.getName());

        if (requestId != null) {
            item.setRequest(requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(String.format("Item request with id: %d is not found", requestId))));
        }

        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));
        Item createdItem = itemRepository.save(item);

        log.debug("Item with id - {} is created.", createdItem.getId());
        return itemMapper.toItemResponseDto(createdItem);
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto itemDto) {
        log.debug("Request to update item with ID - {} is received.", itemId);

        validateUserExists(userId);
        validateItemExists(itemId);

        Item itemForUpdate = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId)));
        validateUserOwnItem(userId, itemForUpdate);

        itemMapper.toItemFromItemUpdateDto(itemDto, itemForUpdate);

        Item updatedItem = itemRepository.save(itemForUpdate);

        log.debug("Item with ID - {} is updated in repository.", updatedItem.getId());
        return itemMapper.toItemResponseDto(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> searchItemsByText(Long userId, String text, Integer from, Integer size) {
        log.debug("A list of all items containing text ({}) in name or description is requested.", text);

        validateUserExists(userId);
        if (!SearchValidator.validateText(text)) {
            return new ArrayList<>();
        }

        List<Item> foundItems;
        if (from == null || size == null) {
            foundItems = itemRepository.searchByNameOrDescription(text);
        } else {
            validatePaginationParameters(from, size);
            Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);
            foundItems = itemRepository.searchByNameOrDescription(text, page);
        }

        log.debug("A list of all items containing text ({}) in name or description is received with size of {}.", text, foundItems.size());
        return foundItems
                .stream()
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(Long userId, Long itemId, Comment comment) {
        log.debug("Request to add comment to item {} by user with id {} received.", itemId, userId);

        validateUserExists(userId);
        validateItemExists(itemId);
        validateUserBookedItemAndBookingEnded(userId, itemId);

        comment.setItem(itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", userId))));
        comment.setAuthor(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));

        Comment createdComment = commentRepository.save(comment);

        log.debug("Comment to item {} is created with id {}.", itemId, createdComment.getId());
        return commentMapper.toCommentResponseDto(createdComment);
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
        if (item.getRequest() != null) {
            responseDto.setRequestId(item.getRequest().getId());
        }

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

    private void validatePaginationParameters(Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new BadRequestException("Incorrect pagination parameters.");
        }
    }

    private boolean checkUserIsOwner(Long userId, Item item) {
        return item.getOwner().getId().equals(userId);
    }
}
