package com.rethreadsinnovations.puppiesapi.service;

import com.rethreadsinnovations.puppiesapi.exceptions.UserNotFoundException;
import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.entities.PostEntity;
import com.rethreadsinnovations.puppiesapi.model.entities.UserEntity;
import com.rethreadsinnovations.puppiesapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private PostEntity postEntity;
    private User user;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("John Doe");
        userEntity.setEmail("johndoe@example.com");

        user = new User(userEntity);

        postEntity = new PostEntity();
        postEntity.setId(1L);
        postEntity.setTextContent("Test");
        postEntity.setDate(LocalDate.now());
        postEntity.setImage(null);
        postEntity.setCreatedBy(userEntity);
        postEntity.setLikedBy(List.of(userEntity));
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        List<UserEntity> entities = List.of(userEntity);
        when(userRepository.findAll()).thenReturn(entities);

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        User result = userService.getUserById(userId);

        assertEquals(userId, result.getId());
        assertEquals("John Doe", result.getName());
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_shouldThrowExceptionWhenNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserByName_shouldReturnListOfUsers() {
        when(userRepository.findByName("John Doe")).thenReturn(Optional.of(List.of(userEntity)));

        List<User> users = userService.getUserByName("John Doe");

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
        verify(userRepository).findByName("John Doe");
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(Optional.of(userEntity));

        User result = userService.getUserByEmail("johndoe@example.com");


        assertEquals("John Doe", result.getName());
        verify(userRepository).findByEmail("johndoe@example.com");
    }

    @Test
    void createUser_shouldReturnUserId() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        Long result = userService.createUser(user);

        assertEquals(userId, result);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User result = userService.updateUser(userId, updatedUser);

        assertEquals("Updated Name", result.getName());
        verify(userRepository).save(userEntity);
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        userService.deleteUser(userId);

        verify(userRepository).delete(userEntity);
    }

    @Test
    void getUserLikes_shouldReturnListOfLikedPosts() {
        userEntity.setLikedPosts(List.of(postEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        List<Post> posts = userService.getUserLikes(userId);

        assertEquals(1, posts.size());
        assertEquals(1L, posts.get(0).getId());
    }

    @Test
    void getUserPosts_shouldReturnListOfPosts() {
        userEntity.setPosts(List.of(postEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        List<Post> posts = userService.getUserPosts(userId);

        assertEquals(1, posts.size());
        assertEquals(1L, posts.get(0).getId());
    }
}
