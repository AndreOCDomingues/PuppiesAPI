package com.rethreadsinnovations.puppiesapi.controller;

import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.request.UserRequest;
import com.rethreadsinnovations.puppiesapi.model.response.PostResponse;
import com.rethreadsinnovations.puppiesapi.model.response.UserResponse;
import com.rethreadsinnovations.puppiesapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User user1;
    private User user2;
    private Post post1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setId(1L);
        user1.setName("User1");
        user1.setEmail("User1@email.com");

        post1 = new Post();
        post1.setId(1L);
        post1.setTextContent("Test content");
        post1.setDate(LocalDate.now());
        post1.setCreatedBy(user1);
        post1.setLikedBy(List.of(user1));

        user2 = new User();
        user2.setId(2L);
        user2.setName("User2");
        user2.setEmail("User2@email.com");
    }

    @Test
    void testGetUsers() {
        List<User> users = List.of(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserResponse>> response = userController.getUsers(null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("User1", response.getBody().get(0).getName());
        assertEquals("User2", response.getBody().get(1).getName());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        Long userId = user1.getId();
        when(userService.getUserById(userId)).thenReturn(user1);

        ResponseEntity<UserResponse> response = userController.getUserById(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User1", response.getBody().getName());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testGetUserLikes() {
        Long userId = user1.getId();
        PostResponse postResponse = new PostResponse(post1);
        List<PostResponse> postResponses = List.of(postResponse);

        when(userService.getUserLikes(userId)).thenReturn(List.of());

        ResponseEntity<List<PostResponse>> response = userController.getUserLikes(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(userService, times(1)).getUserLikes(userId);
    }

    @Test
    void testGetUserPosts() {
        Long userId = user1.getId();
        PostResponse postResponse = new PostResponse(post1); // Suponha que PostResponse tenha um construtor padrão
        List<PostResponse> postResponses = List.of(postResponse);

        when(userService.getUserPosts(userId)).thenReturn(List.of());

        ResponseEntity<List<PostResponse>> response = userController.getUserPosts(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(userService, times(1)).getUserPosts(userId);
    }

    @Test
    void testCreateUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("User1");
        userRequest.setEmail("User1@email.com");
        Long userId = 1L;

        when(userService.createUser(any(User.class))).thenReturn(userId);

        ResponseEntity<Object> response = userController.createUser(userRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("/1", response.getHeaders().getLocation().getPath());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest();
        userRequest.setName("UpdatedUser");
        userRequest.setEmail("UpdatedUser@email.com");
        User user = new User();
        user.setName("UpdatedUser");
        user.setEmail("UpdatedUser@email.com");
        user.setId(userId);

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(user);

        ResponseEntity<UserResponse> response = userController.updateUser(userId, userRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("UpdatedUser", response.getBody().getName());
        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(userId);
    }
}
