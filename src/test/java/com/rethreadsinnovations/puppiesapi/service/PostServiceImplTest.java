package com.rethreadsinnovations.puppiesapi.service;

import com.rethreadsinnovations.puppiesapi.exceptions.PostNotFoundException;
import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.entities.PostEntity;
import com.rethreadsinnovations.puppiesapi.model.entities.UserEntity;
import com.rethreadsinnovations.puppiesapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private UserEntity userEntity;
    private PostEntity postEntity;
    private Post post;
    private final Long postId = 1L;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("John Doe");
        userEntity.setEmail("johndoe@example.com");

        postEntity = new PostEntity();
        postEntity.setId(postId);
        postEntity.setTextContent("Test content");
        postEntity.setDate(LocalDate.now());
        postEntity.setCreatedBy(userEntity);
        postEntity.setLikedBy(List.of(userEntity));

        post = new Post(postEntity);
    }

    @Test
    void createPost_shouldReturnCreatedPost() throws IOException {
        when(postRepository.save(any(PostEntity.class))).thenReturn(postEntity);
        
        Post result = postService.createPost(post);
        
        assertNotNull(result);
        assertEquals(postId, result.getId());
        assertEquals("Test content", result.getTextContent());
        verify(postRepository).save(any(PostEntity.class));
    }

    @Test
    void likePost_shouldAddLikeToPost() {
        User user = new User();
        user.setId(2L);
        user.setName("James");
        user.setEmail("james@email.com");

        postEntity.setLikedBy(new ArrayList<>());

        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postRepository.save(postEntity)).thenReturn(postEntity);

        postService.likePost(postId, user);

        assertEquals(1, postEntity.getLikedBy().size());
        verify(postRepository).save(postEntity);
    }

    @Test
    void getPost_shouldReturnPostById() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        
        Post result = postService.getPost(postId);
        
        assertNotNull(result);
        assertEquals(postId, result.getId());
        verify(postRepository).findById(postId);
    }

    @Test
    void getPost_shouldThrowExceptionWhenPostNotFound() {
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        
        assertThrows(PostNotFoundException.class, () -> postService.getPost(postId));
        verify(postRepository).findById(postId);
    }

    @Test
    void getAllPosts_shouldReturnListOfPostsOrderedByDate() {
        PostEntity post1 = new PostEntity();
        post1.setId(1L);
        post1.setTextContent("First post");
        post1.setDate(LocalDate.now().minusDays(1));
        post1.setCreatedBy(userEntity);
        post1.setLikedBy(List.of(userEntity));

        PostEntity post2 = new PostEntity();
        post2.setId(2L);
        post2.setTextContent("Second post");
        post2.setDate(LocalDate.now());
        post2.setCreatedBy(userEntity);
        post2.setLikedBy(List.of(userEntity));

        List<PostEntity> posts = List.of(post1, post2);

        when(postRepository.findAll()).thenReturn(posts);
        
        List<Post> result = postService.getAllPosts();
        
        assertEquals(2, result.size());
        assertEquals("Second post", result.get(0).getTextContent());
        assertEquals("First post", result.get(1).getTextContent());
        verify(postRepository).findAll();
    }
}
