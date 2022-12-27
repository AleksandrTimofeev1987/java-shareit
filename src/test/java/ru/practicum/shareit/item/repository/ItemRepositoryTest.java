package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class ItemRepositoryTest {

    private User genericUser;
    private Item genericItem;

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

    }

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testSearchByNameOrDescription() {
        //given
        List<Item> listToGet = new ArrayList<>();
        listToGet.add(genericItem);
        userRepository.save(genericUser);
        itemRepository.save(genericItem);

        //when
        List<Item> result = itemRepository.searchByNameOrDescription("Am");

        //then
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testSearchByNameOrDescriptionPaged() {
        //given
        List<Item> listToGet = new ArrayList<>();
        listToGet.add(genericItem);
        userRepository.save(genericUser);
        itemRepository.save(genericItem);
        Pageable page = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));

        //when
        List<Item> result = itemRepository.searchByNameOrDescription("Am", page);

        //then
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testCountItemsOwnedByUser() {
        //given
        User user = userRepository.save(genericUser);
        itemRepository.save(genericItem);

        //when
        Long result = itemRepository.countItemsOwnedByUser(user.getId());

        //then
        Assertions.assertEquals(1L, result);
    }
}
