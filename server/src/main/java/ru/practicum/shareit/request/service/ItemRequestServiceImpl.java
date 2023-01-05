package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private static final Sort SORT_BY_CREATED = Sort.by(Sort.Direction.DESC, "created");
    private final ItemRepository itemRepository;
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper requestMapper;
    private final ItemMapper itemMapper;


    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> getAllItemRequestsByRequesterId(Long userId) {
        log.debug("A list of all item requests created by user with ID - {} is requested.", userId);

        validateUserExists(userId);

        List<ItemRequest> foundItemRequests = requestRepository.findByRequesterIdOrderByCreatedDesc(userId);

        log.debug("A list of all item requests created by user with ID - {} is received with size of {}.", userId, foundItemRequests.size());
        return foundItemRequests
                .stream()
                .map(this::toItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        log.debug("A list of all item requests created by other users is requested by user with ID - {}.", userId);

        validateUserExists(userId);

        List<ItemRequest> foundItemRequests;

        Pageable page = PageRequest.of(from / size, size, SORT_BY_CREATED);

        Page<ItemRequest> foundItemRequestsPage = requestRepository.findAll(page);
        foundItemRequests = getFilteredItemRequests(userId, foundItemRequestsPage.getContent());

        log.debug("A list of all item requests created by other users is received with size of {}.", foundItemRequests.size());
        return foundItemRequests
                .stream()
                .map(this::toItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestResponseDto getItemRequestById(Long userId, Long requestId) {
        log.debug("Item request with ID - {} is requested.", requestId);

        validateUserExists(userId);

        ItemRequest foundItemRequest = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(String.format("Item request with id: %d is not found", requestId)));

        ItemRequestResponseDto result = toItemRequestResponseDto(foundItemRequest);

        log.debug("Item request with ID - {} is received.", result.getId());
        return result;
    }

    @Override
    @Transactional
    public ItemRequestResponseDto createItemRequest(Long userId, ItemRequest itemRequest) {
        log.debug("Request to add item request by user with id - {} is received.", userId);

        itemRequest.setRequester(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));

        ItemRequest createdItemRequest = requestRepository.save(itemRequest);

        log.debug("Item request with id - {} is created.", createdItemRequest.getId());
        return requestMapper.toItemRequestResponseDto(createdItemRequest);
    }

    private ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto itemRequestResponseDto = requestMapper.toItemRequestResponseDto(itemRequest);
        List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());

        itemRequestResponseDto.setItems(items
                .stream()
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList()));

        return itemRequestResponseDto;
    }

    private List<ItemRequest> getFilteredItemRequests(Long userId, List<ItemRequest> foundItemRequests) {
        return foundItemRequests
                .stream()
                .filter(request -> !request.getRequester().getId().equals(userId))
                .collect(Collectors.toList());
    }

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id: %d is not found", userId));
        }
    }
}
