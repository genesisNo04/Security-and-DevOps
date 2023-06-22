package com.example.demo.Controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);



    @Before
    public void setUp() {
        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void cartControllerTest() throws IOException {
        User user = new User();
        user.setUsername("testCart");

        Cart cart = new Cart();
        user.setCart(cart);

        Item items = new Item();
        items.setDescription("Toys");
        items.setId(0L);
        items.setName("Mermaid");
        items.setPrice(new BigDecimal(50.00));

        cart.addItem(items);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("testCart");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());

        responseEntity = cartController.removeFromcart(modifyCartRequest);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());

    }
}
