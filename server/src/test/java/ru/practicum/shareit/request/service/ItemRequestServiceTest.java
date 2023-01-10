package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {

    private User genericUser;
    private User genericOwner;
    private final UserResponseDto genericOwnerResponseDto = new UserResponseDto(2L, "owner", "owner@mail.ru");
    private Item genericItem;
    private ItemResponseDto genericItemResponseDto;
    private ItemRequest genericRequest;
    private ItemRequestResponseDto genericRequestResponseDto;

    @InjectMocks
    private ItemRequestServiceImpl service;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestMapper requestMapper;

    @Mock
    private ItemMapper itemMapper;

    @BeforeEach
    public void beforeEach() {
        genericUser = new User();
        genericUser.setId(1L);
        genericUser.setName("name");
        genericUser.setEmail("email@mail.ru");

        genericOwner = new User();
        genericOwner.setId(2L);
        genericOwner.setName("owner");
        genericOwner.setEmail("owner@mail.ru");

        genericRequest = new ItemRequest();
        genericRequest.setId(1L);
        genericRequest.setDescription("description");
        genericRequest.setRequester(genericUser);
        genericRequest.setCreated(LocalDateTime.now());

        genericItem = new Item();
        genericItem.setId(1L);
        genericItem.setName("name");
        genericItem.setDescription("description");
        genericItem.setOwner(genericOwner);
        genericItem.setAvailable(true);
        genericItem.setRequest(genericRequest);

        genericRequestResponseDto = new ItemRequestResponseDto(1L, "description", genericOwnerResponseDto, genericRequest.getCreated(), null);
        genericItemResponseDto = new ItemResponseDto(1L, "name", "description", genericOwnerResponseDto, true, 1L, null, null, new HashSet<>());

    }

    @Test
    public void testGetAllItemRequestsByRequesterId() {
        List<ItemRequest> foundRequests = new ArrayList<>();
        foundRequests.add(genericRequest);
        List<ItemRequestResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericRequestResponseDto);
        List<Item> items = new ArrayList<>();
        items.add(genericItem);

        Mockito
                .when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        Mockito
                .when(requestRepository.findByRequesterIdOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(foundRequests);

        Mockito
                .when(itemRepository.findAllByRequestId(Mockito.anyLong()))
                .thenReturn(items);

        Mockito
                .when(itemMapper.toItemResponseDto(Mockito.any(Item.class)))
                .thenReturn(genericItemResponseDto);

        Mockito
                .when(requestMapper.toItemRequestResponseDto(Mockito.any(ItemRequest.class)))
                .thenReturn(genericRequestResponseDto);

        List<ItemRequestResponseDto> result = service.getAllItemRequestsByRequesterId(1L);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetAllItemRequests() {
        List<ItemRequest> foundRequests = new ArrayList<>();
        foundRequests.add(genericRequest);
        Page<ItemRequest> foundRequestsPage = new PageImpl<>(foundRequests);
        List<ItemRequestResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericRequestResponseDto);
        List<Item> items = new ArrayList<>();
        items.add(genericItem);

        Mockito
                .when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        Mockito
                .when(requestRepository.findItemRequestsByRequester_IdIsNot(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(foundRequestsPage);

        Mockito
                .when(requestMapper.toItemRequestResponseDto(genericRequest))
                .thenReturn(genericRequestResponseDto);

        Mockito
                .when(itemRepository.findAllByRequestId(1L))
                .thenReturn(items);

        Mockito
                .when(itemMapper.toItemResponseDto(Mockito.any(Item.class)))
                .thenReturn(genericItemResponseDto);

        List<ItemRequestResponseDto> result = service.getAllItemRequests(2L, 0, 10);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetItemRequestById() {
        List<Item> items = new ArrayList<>();
        items.add(genericItem);

        Mockito
                .when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        Mockito
                .when(requestRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericRequest));

        Mockito
                .when(itemRepository.findAllByRequestId(1L))
                .thenReturn(items);

        Mockito
                .when(itemMapper.toItemResponseDto(genericItem))
                .thenReturn(genericItemResponseDto);

        Mockito
                .when(requestMapper.toItemRequestResponseDto(genericRequest))
                .thenReturn(genericRequestResponseDto);

        ItemRequestResponseDto result = service.getItemRequestById(1L, 1L);
        Assertions.assertEquals(genericRequestResponseDto, result);
    }

    @Test
    public void testCreateItemRequest() {
        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        Mockito
                .when(requestRepository.save(genericRequest))
                .thenReturn(genericRequest);

        Mockito
                .when(requestMapper.toItemRequestResponseDto(genericRequest))
                .thenReturn(genericRequestResponseDto);

        ItemRequestResponseDto result = service.createItemRequest(1L, genericRequest);
        Assertions.assertEquals(genericRequestResponseDto, result);
    }
}
