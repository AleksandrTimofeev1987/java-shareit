package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.model.ItemWithBookingsAndComments;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.validation.SearchValidator;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingsAndComments> getAllItemsByUserId(Long userId) {
        log.debug("A list of all items owned by user with ID - {} is requested.", userId);
        validateUserExists(userId);
        List<ItemEntity> items = itemRepository.findByOwnerId(userId);
        List<ItemWithBookingsAndComments> itemsWithoutBookingsAndComments = items
                .stream()
                .map(this::mapItemToItemWithBookings)
                .collect(Collectors.toList());

        List<ItemWithBookingsAndComments> result = itemsWithoutBookingsAndComments
                .stream()
                .map(this::populateItemWithBookings)
                .collect(Collectors.toList());

        log.debug("A list of all items owned by user with ID - {} is received with size of {}.", userId, items.size());
        return result;
    }

    @Override
    public ItemWithBookingsAndComments getItemById(Long userId, Long itemId) {
        log.debug("Item with ID - {} is requested.", itemId);
        validateUserExists(userId);
        Optional<ItemEntity> itemOpt = itemRepository.findById(itemId);
        validateItemExists(itemId, itemOpt);

        ItemWithBookingsAndComments itemWithoutBookingsAndComments = mapItemToItemWithBookings(itemOpt.get());
        ItemWithBookingsAndComments itemWithoutComments = checkUserIsOwner(userId, itemWithoutBookingsAndComments) ? populateItemWithBookings(itemWithoutBookingsAndComments) : itemWithoutBookingsAndComments;

        return populateItemWithComments(itemWithoutComments);
    }

    @Override
    public ItemEntity createItem(Long userId, ItemEntity item) {
        log.debug("Request to add item with name - {} is received.", item.getName());
        validateUserExists(userId);
        item.setOwnerId(userId);
        ItemEntity addedItem = itemRepository.save(item);
        log.debug("Item with ID - {} is added to repository.", item.getId());
        return addedItem;
    }

    @Override
    public ItemEntity updateItem(Long userId, Long itemId, ItemUpdateDto itemDto) {
        log.debug("Request to update item with ID - {} is received.", itemId);
        validateUserExists(userId);
        Optional<ItemEntity> itemOpt = itemRepository.findById(itemId);
        validateItemExists(itemId, itemOpt);
        ItemEntity itemForUpgrade = itemOpt.get();

        validateUserOwnItem(userId, itemForUpgrade);

        mapper.toItemEntityFromItemUpdateDto(itemDto, itemForUpgrade);

        ItemEntity updatedItem = itemRepository.save(itemForUpgrade);
        log.debug("Item with ID - {} is updated in repository.", itemId);
        return updatedItem;
    }

    @Override
    public List<ItemEntity> searchItemsByText(Long userId, String text) {
        log.debug("A list of all items containing text ({}) in name or description is requested.", text);
        validateUserExists(userId);
        if (!SearchValidator.validateText(text)) {
            return new ArrayList<>();
        }

        List<ItemEntity> items = itemRepository.searchItemsByText(text);

        log.debug("A list of all items containing text ({}) in name or description is received with size of {}.", text, items.size());
        return items;
    }

    @Override
    public Comment createComment(CommentEntity comment) {
        log.debug("Request to add comment is received.");
        validateUserExists(comment.getAuthorId());
        validateItemExistsInDB(comment.getItemId());
        validateUserBookedItemAndBookingEnded(comment.getAuthorId(), comment.getItemId());

        CommentEntity savedComment = commentRepository.save(comment);

        return mapCommentEntityToComment(savedComment);
    }

    private ItemWithBookingsAndComments mapItemToItemWithBookings(ItemEntity savedItem) {
        if (savedItem == null) {
            return null;
        }

        ItemWithBookingsAndComments item = new ItemWithBookingsAndComments();

        item.setId(savedItem.getId());
        item.setName(savedItem.getName());
        item.setDescription(savedItem.getDescription());
        item.setOwnerId(savedItem.getOwnerId());
        item.setAvailable(savedItem.getAvailable());

        return item;
    }

    private Comment mapCommentEntityToComment(CommentEntity savedComment) {
        if (savedComment == null) {
            return null;
        }

        Comment comment = new Comment();

        comment.setId(savedComment.getId());
        comment.setText(savedComment.getText());
        comment.setAuthorName(userRepository.getUserNameById(savedComment.getAuthorId()));
        comment.setCreated(savedComment.getCreated());

        return comment;
    }

    private ItemWithBookingsAndComments populateItemWithBookings(ItemWithBookingsAndComments itemWithoutBookingsAndComments) {
        setItemLastAndNextBooking(itemWithoutBookingsAndComments);
        return itemWithoutBookingsAndComments;
    }

    private void setItemLastAndNextBooking(ItemWithBookingsAndComments item) {
        List<BookingShort> allBookingByItemId = bookingRepository.findByItemId(item.getId());
        LocalDateTime now = LocalDateTime.now();

        setItemLastBooking(item, allBookingByItemId, now);

        setItemNextBooking(item, allBookingByItemId, now);
    }

    private static void setItemLastBooking(ItemWithBookingsAndComments item, List<BookingShort> allBookingByItemId, LocalDateTime now) {
        item.setLastBooking(allBookingByItemId
                .stream()
                .filter(booking -> booking.getEnd().isBefore(now))
                .max(Comparator.comparing(BookingShort::getEnd)).orElse(null));
    }

    private static void setItemNextBooking(ItemWithBookingsAndComments item, List<BookingShort> allBookingByItemId, LocalDateTime now) {
        item.setNextBooking(allBookingByItemId
                .stream()
                .filter(booking -> booking.getStart().isAfter(now))
                .min(Comparator.comparing(BookingShort::getEnd)).orElse(null));
    }

    private ItemWithBookingsAndComments populateItemWithComments(ItemWithBookingsAndComments itemWithoutComments) {
        List<CommentEntity> savedComments = commentRepository.findByItemId(itemWithoutComments.getId());
        List<Comment> comments = savedComments
                .stream()
                .map(this::mapCommentEntityToComment)
                .collect(Collectors.toList());
        itemWithoutComments.setComments(comments);
        return itemWithoutComments;
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id: %d is not found", userId));
        }
    }

    private void validateItemExists(Long itemId, Optional<ItemEntity> itemOpt) {
        if (!itemOpt.isPresent()) {
            throw new NotFoundException(String.format("Item with id: %d is not found", itemId));
        }
    }

    private void validateItemExistsInDB(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(String.format("Item with id: %d is not found", itemId));
        }
    }

    private void validateUserOwnItem(Long userId, ItemEntity itemForUpgrade) {
        if (!itemForUpgrade.getOwnerId().equals(userId)) {
            throw new ForbiddenException(String.format("User with id: %d does not own item with id: %d", userId, itemForUpgrade.getId()));
        }
    }

    private boolean checkUserIsOwner(Long userId, ItemWithBookingsAndComments itemWithoutBookings) {
        return itemWithoutBookings.getOwnerId().equals(userId);
    }

    private void validateUserBookedItemAndBookingEnded(Long authorId, Long itemId) {
        if (bookingRepository.findByBookerIdItemIdAndPast(authorId, itemId, LocalDateTime.now()).size() < 1) {
            throw new BadRequestException("Comment cannot be published for unfinished or future booking");
        }
    }
}
