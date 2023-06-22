package com.example.demo.Controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void itemControllerTest(){
        Item items = new Item();
        items.setDescription("Book");
        items.setId(0L);
        items.setName("The Power of Now");
        items.setPrice(new BigDecimal(50.00));
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(items));
        ResponseEntity<Item> responseEntity = itemController.getItemById(0L);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());

        when(itemRepository.findByName(anyString())).thenReturn(itemList);
        ResponseEntity<List<Item>> responseEntityList = itemController.getItemsByName(items.getName());
        Assert.assertNotNull(responseEntityList);
        Assert.assertEquals(200, responseEntityList.getStatusCodeValue());
    }
}
