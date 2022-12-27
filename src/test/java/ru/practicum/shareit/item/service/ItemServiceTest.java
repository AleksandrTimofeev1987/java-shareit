package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    private User genericUser;
    private final UserResponseDto genericUserResponseDto = new UserResponseDto(1L, "name", "email@mail.ru");
    private Item genericItem;
    private ItemResponseDto genericItemResponseDto;
    private ItemRequest genericItemRequest;
    private Comment genericComment;
    private final CommentResponseDto genericCommentResponseDto = new CommentResponseDto(1L, "text", "name", LocalDateTime.now());

    @InjectMocks
    private ItemServiceImpl service;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private BookingMapper bookingMapper;

    @BeforeEach
    public void beforeEach() {
        genericUser = new User();
        genericUser.setId(1L);
        genericUser.setName("name");
        genericUser.setEmail("email@mail.ru");

        genericItem = new Item();
        genericItem.setId(1L);
        genericItem.setName("name");
        genericItem.setDescription("description");
        genericItem.setOwner(genericUser);
        genericItem.setAvailable(true);

        genericItemResponseDto = new ItemResponseDto(1L, "name", "description", genericUserResponseDto, true, null, null, null, new HashSet<>());

        genericItemRequest = new ItemRequest();
        genericItemRequest.setId(1L);
        genericItemRequest.setDescription("description");
        genericItemRequest.setRequester(genericUser);
        genericItemRequest.setCreated(LocalDateTime.now());

        genericComment = new Comment();
        genericComment.setId(1L);
        genericComment.setText("text");
        genericComment.setItem(genericItem);
        genericComment.setAuthor(genericUser);
        genericComment.setCreated(genericCommentResponseDto.getCreated());
    }

    @Test
    public void testGetAllItemsByUserId() {
        List<Item> foundItems = new ArrayList<>();
        foundItems.add(genericItem);
        List<ItemResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericItemResponseDto);

        Mockito
                .when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        Mockito
                .when(itemRepository.findByOwnerIdOrderById(Mockito.anyLong()))
                .thenReturn(foundItems);

        Mockito
                .when(userMapper.toUserResponseDto(Mockito.any(User.class)))
                .thenReturn(genericUserResponseDto);

        List<ItemResponseDto> result = service.getAllItemsByUserId(1L, null, null);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetItemById() {
        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericItem));

        Mockito
                .when(userMapper.toUserResponseDto(genericUser))
                .thenReturn(genericUserResponseDto);

        ItemResponseDto result = service.getItemById(1L, 1L);
        Assertions.assertEquals(genericItemResponseDto, result);
    }

    @Test
    public void testCreateItem() {
        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        Mockito
                .when(itemRepository.save(genericItem))
                .thenReturn(genericItem);

        Mockito
                .when(itemMapper.toItemResponseDto(genericItem))
                .thenReturn(genericItemResponseDto);

        ItemResponseDto result = service.createItem(1L, genericItem, null);
        Assertions.assertEquals(genericItemResponseDto, result);
    }

    @Test
    public void testCreateItemWithRequest() {
        genericItemResponseDto.setRequestId(1L);

        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        Mockito
                .when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(genericItem);

        Mockito
                .when(itemMapper.toItemResponseDto(genericItem))
                .thenReturn(genericItemResponseDto);

        Mockito
                .when(requestRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericItemRequest));

        ItemResponseDto result = service.createItem(1L, genericItem, 1L);
        Assertions.assertEquals(genericItemResponseDto, result);
    }

    @Test
    public void testUpdateItem() {
        final ItemUpdateDto itemUpdateDto = new ItemUpdateDto(1L, "updated_name", "updated_description", false);
        final Item updatedItem = new Item();
        genericItem = new Item();
        genericItem.setId(1L);
        genericItem.setName("updated_name");
        genericItem.setDescription("updated_description");
        genericItem.setOwner(genericUser);
        genericItem.setAvailable(false);
        ItemResponseDto itemResponseDto = new ItemResponseDto(1L, "updated_name", "updated_description", genericUserResponseDto, false, null, null, null, null);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericItem));

        Mockito
                .when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(updatedItem);

        Mockito
                .when(itemMapper.toItemResponseDto(updatedItem))
                .thenReturn(itemResponseDto);

        ItemResponseDto result = service.updateItem(1L, 1L, itemUpdateDto);
        Assertions.assertEquals(itemResponseDto, result);
    }

    @Test
    public void testCreateComment() {
        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(bookingRepository.countByBookerIdItemIdAndPast(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(1L);

        Mockito
                .when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericItem));

        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        Mockito
                .when(commentRepository.save(genericComment))
                .thenReturn(genericComment);

        Mockito
                .when(commentMapper.toCommentResponseDto(genericComment))
                .thenReturn(genericCommentResponseDto);

        CommentResponseDto result = service.createComment(1L, 1L, genericComment);
        Assertions.assertEquals(genericCommentResponseDto, result);
    }

    @Test
    public void testSearchItemByText() {
        List<Item> foundItems = new ArrayList<>();
        foundItems.add(genericItem);
        List<ItemResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericItemResponseDto);

        Mockito
                .when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito
                .when(itemRepository.searchByNameOrDescription(Mockito.anyString()))
                .thenReturn(foundItems);

        Mockito
                .when(itemMapper.toItemResponseDto(genericItem))
                .thenReturn(genericItemResponseDto);

        List<ItemResponseDto> result = service.searchItemsByText(1L, "search", null, null);
        Assertions.assertEquals(listToGet, result);
    }
}
