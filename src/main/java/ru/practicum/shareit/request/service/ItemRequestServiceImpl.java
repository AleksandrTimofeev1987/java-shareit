package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper mapper;


    @Override
    public List<ItemRequestResponseDto> getAllItemRequestsByRequesterId(Long userId) {
        log.debug("A list of all item requests created by user with ID - {} is requested.", userId);

        validateUserExists(userId);

        List<ItemRequest> foundItemRequests = requestRepository.findByRequesterIdOrderByCreatedDesc(userId);

        log.debug("A list of all item requests created by user with ID - {} is received with size of {}.", userId, foundItemRequests.size());
        return foundItemRequests
                .stream()
                .map(mapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponseDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestResponseDto getItemRequestById(Long userId, Long requestId) {
        log.debug("Item request with ID - {} is requested.", requestId);

        validateUserExists(userId);

        ItemRequest foundItemRequest = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(String.format("Item request with id: %d is not found", requestId)));

        ItemRequestResponseDto result = mapper.toItemRequestResponseDto(foundItemRequest);

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
        return mapper.toItemRequestResponseDto(createdItemRequest);
    }

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id: %d is not found", userId));
        }
    }
}
