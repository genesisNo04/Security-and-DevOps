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
    public void orderControllerTest(){

        User user = new User();
        user.setUsername("test");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        Cart cart = new Cart();

        Item items = new Item();
        items.setDescription("Book");
        items.setId(0L);
        items.setName("12 Rules of Life");
        items.setPrice(new BigDecimal(50.00));

        cart.addItem(items);
        user.setCart(cart);
        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit(user.getUsername());
        Assert.assertNotNull(userOrderResponseEntity);
        Assert.assertEquals(200, userOrderResponseEntity.getStatusCodeValue());

        ResponseEntity<List<UserOrder>> ordersList = orderController.getOrdersForUser(user.getUsername());

        Assert.assertNotNull(ordersList);
        Assert.assertEquals(200, ordersList.getStatusCodeValue());

    }
}
