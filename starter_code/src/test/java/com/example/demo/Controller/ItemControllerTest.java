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

    @Test
    public void getItems (){
        Item item0 = new Item();
        item0.setId(1L);
        item0.setName("Round Widget");
        item0.setDescription("A widget that is Round");
        item0.setPrice(new BigDecimal("2.99"));

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Square Widget");
        item1.setDescription("A widget that is square");
        item1.setPrice(new BigDecimal("1.99"));

        List<Item> itemList = new ArrayList<>();
        itemList.add(0, item0);
        itemList.add(1, item1);
        when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        List<Item> resultItems = responseEntity.getBody();
        Assert.assertNotNull(resultItems);
        Assert.assertEquals(item0, resultItems.get(0));
        Assert.assertEquals(item1, resultItems.get(1));
    }

    @Test
    public void testGetItemById(){
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Square Widget");
        item1.setDescription("A widget that is square");
        item1.setPrice(new BigDecimal("1.99"));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item1));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Item obtainedItem= response.getBody();
        Assert.assertNotNull(obtainedItem);
        Assert.assertEquals(item1, obtainedItem);
    }

    @Test
    public void testGetItemByName(){
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Square Widget");
        item1.setDescription("A widget that is square");
        item1.setPrice(new BigDecimal("1.99"));
        List<Item> itemList= new ArrayList<>(); itemList.add(item1);
        when(itemRepository.findByName("Square Widget")).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Square Widget");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<Item> obtainedItems= response.getBody();
        Assert.assertNotNull(obtainedItems);
        Assert.assertEquals(item1, obtainedItems.get(0));
    }
}
