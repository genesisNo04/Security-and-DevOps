package com.example.demo.Controllers;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private ItemController itemController;
    private CartController cartController;
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setUp() {
        userController = new UserController();
        itemController = new ItemController();
        orderController = new OrderController();
        cartController = new CartController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void createUserHappyPath() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0, u.getId());
        Assert.assertEquals("test", u.getUsername());
        Assert.assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void createUserIncorrectConfirmPassword() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test1");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword123");

        final ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());
    }

//    @Test
//    public void itemControllerTest(){
//        Item items = new Item();
//        items.setDescription("Book");
//        items.setId(0L);
//        items.setName("The Power of Now");
//        items.setPrice(new BigDecimal(50.00));
//        List<Item> itemList = new ArrayList<>();
//        itemList.add(items);
//        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(items));
//        ResponseEntity<Item> responseEntity = itemController.getItemById(0L);
//        Assert.assertNotNull(responseEntity);
//        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
//
//        when(itemRepository.findByName(anyString())).thenReturn(itemList);
//        ResponseEntity<List<Item>> responseEntityList = itemController.getItemsByName(items.getName());
//        Assert.assertNotNull(responseEntityList);
//        Assert.assertEquals(200, responseEntityList.getStatusCodeValue());
//    }
//
//    @Test
//    public void cartControllerTest() throws IOException {
//        User user = new User();
//        user.setUsername("test");
//
//        Cart cart = new Cart();
//        user.setCart(cart);
//
//        Item items = new Item();
//        items.setDescription("Book");
//        items.setId(0L);
//        items.setName("Beloved");
//        items.setPrice(new BigDecimal(50.00));
//
//        cart.addItem(items);
//
//        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
//        modifyCartRequest.setItemId(0L);
//        modifyCartRequest.setUsername("test");
//        modifyCartRequest.setQuantity(1);
//
//        when(userRepository.findByUsername(anyString())).thenReturn(user);
//        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
//        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
//        Assert.assertNotNull(responseEntity);
//        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
//
//        responseEntity = cartController.removeFromcart(modifyCartRequest);
//        Assert.assertNotNull(responseEntity);
//        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
//
//    }
//
//    @Test
//    public void orderControllerTest(){
//
//        User user = new User();
//        user.setUsername("test");
//        when(userRepository.findByUsername(anyString())).thenReturn(user);
//        Cart cart = new Cart();
//
//        Item items = new Item();
//        items.setDescription("Book");
//        items.setId(0L);
//        items.setName("12 Rules of Life");
//        items.setPrice(new BigDecimal(50.00));
//
//        cart.addItem(items);
//        user.setCart(cart);
//        ResponseEntity<UserOrder> userOrderResponseEntity = orderController.submit(user.getUsername());
//        Assert.assertNotNull(userOrderResponseEntity);
//        Assert.assertEquals(200, userOrderResponseEntity.getStatusCodeValue());
//
//        ResponseEntity<List<UserOrder>> ordersList = orderController.getOrdersForUser(user.getUsername());
//
//        Assert.assertNotNull(ordersList);
//        Assert.assertEquals(200, ordersList.getStatusCodeValue());
//
//    }
}
