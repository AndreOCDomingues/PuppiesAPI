package com.rethreadsinnovations.puppiesapi.controller;

import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.response.PostResponse;
import com.rethreadsinnovations.puppiesapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost() throws IOException {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "image data".getBytes()
        );

        String textContent = "My new post";
        LocalDate date = LocalDate.now();
        Post post = new Post(textContent, image.getBytes(), date, user);
        post.setId(1L);

        when(postService.createPost(any(Post.class))).thenReturn(post);

        ResponseEntity<Void> response = postController.createPost(image, textContent, date, user);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(URI.create("/1"), response.getHeaders().getLocation());
        verify(postService, times(1)).createPost(any(Post.class));
    }

    @Test
    void testLikePost() {
        Long postId = 1L;

        doNothing().when(postService).likePost(postId, user);

        ResponseEntity<Void> response = postController.likePost(postId, user);

        assertEquals(200, response.getStatusCodeValue());
        verify(postService, times(1)).likePost(eq(postId), eq(user));
    }

    @Test
    void testGetPost() {
        Long postId = 1L;
        Post post = new Post("Test content", new byte[]{}, LocalDate.now(), user);
        post.setId(postId);

        when(postService.getPost(postId)).thenReturn(post);

        ResponseEntity<PostResponse> response = postController.getPost(postId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test content", response.getBody().getTextContent());
        assertEquals(postId, response.getBody().getId());
        verify(postService, times(1)).getPost(postId);
    }

    @Test
    void testGetAllPosts() {
        Post post1 = new Post("Content 1", new byte[]{}, LocalDate.now(), user);
        post1.setId(1L);

        Post post2 = new Post("Content 2", new byte[]{}, LocalDate.now(), user);
        post2.setId(2L);

        List<Post> posts = List.of(post1, post2);

        when(postService.getAllPosts()).thenReturn(posts);

        ResponseEntity<List<PostResponse>> response = postController.getAllPosts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Content 1", response.getBody().get(0).getTextContent());
        assertEquals("Content 2", response.getBody().get(1).getTextContent());
        verify(postService, times(1)).getAllPosts();
    }
}