package com.example.demo.Controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp() {
        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
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
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test1");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword123");

        final ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createPasswordLessThanSeven() throws Exception {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("test");
        user.setConfirmPassword("test");

        final ResponseEntity<User> response = userController.createUser(user);

        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUsername() {
        when(encoder.encode("test1234")).thenReturn("thisIsHashed");
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("test1234");
        user.setConfirmPassword("test1234");
        final ResponseEntity<User> response = userController.createUser(user);
        User userResult = response.getBody();
        when(userRepository.findByUsername("test")).thenReturn(userResult);

        final ResponseEntity<User> responseTest = userController.findByUserName(user.getUsername());

        User u = responseTest.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0, u.getId());
        Assert.assertEquals("test", u.getUsername());
        Assert.assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void testFindById(){
        when(encoder.encode("test1234")).thenReturn("thisIsHashed");
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("test");
        user.setPassword("test1234");
        user.setConfirmPassword("test1234");
        final ResponseEntity<User> response = userController.createUser(user);
        User userResult = response.getBody();
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(userResult));

        final ResponseEntity<User> responseTest = userController.findById(0L);

        User userResponse = responseTest.getBody();
        Assert.assertNotNull(userResponse);
        Assert.assertEquals(0, userResponse.getId());
        Assert.assertEquals("test", userResponse.getUsername());
        Assert.assertEquals("thisIsHashed", userResponse.getPassword());
    }
}
