package com.example.demo.Controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void testSubmitUserNull() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(0L);

        Item item = new Item();
        item.setDescription("Toy");
        item.setId(0L);
        item.setName("Mermaid");
        item.setPrice(new BigDecimal(50.00));

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("5.00"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<UserOrder> response =  orderController.submit(username);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void orderControllerTest(){

        User user = new User();
        user.setUsername("test");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        Cart cart = new Cart();

        Item item = new Item();
        item.setDescription("Toy");
        item.setId(0L);
        item.setName("Mermaid");
        item.setPrice(new BigDecimal(50.00));

        cart.addItem(item);
        user.setCart(cart);
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit(user.getUsername());
        Assert.assertNotNull(userOrderResponseEntity);
        Assert.assertEquals(200, userOrderResponseEntity.getStatusCodeValue());

        ResponseEntity<List<UserOrder>> ordersList = orderController.getOrdersForUser(user.getUsername());

        Assert.assertNotNull(ordersList);
        Assert.assertEquals(200, ordersList.getStatusCodeValue());

    }

    @Test
    public void testGetOrdersForUserNull() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test123");
        user.setId(0L);

        Item item = new Item();
        item.setDescription("Toy");
        item.setId(0L);
        item.setName("Mermaid");
        item.setPrice(new BigDecimal(50.00));

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("2.99"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(null);

        orderController.submit("test");

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("test");

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUser() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(0L);

        Item item = new Item();
        item.setDescription("Toy");
        item.setId(0L);
        item.setName("Mermaid");
        item.setPrice(new BigDecimal(50.00));

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("2.99"));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername(username)).thenReturn(user);

        orderController.submit(username);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        List<UserOrder> userOrders = responseEntity.getBody();
        Assert.assertNotNull(userOrders);
        Assert.assertEquals(0, userOrders.size());
    }
}
