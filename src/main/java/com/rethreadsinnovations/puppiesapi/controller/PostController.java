package com.rethreadsinnovations.puppiesapi.controller;

import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.response.PostResponse;
import com.rethreadsinnovations.puppiesapi.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Void> createPost(
            @RequestParam("image") MultipartFile image,
            @RequestParam("textContent") String textContent,
            @RequestParam("date") LocalDate date,
            @AuthenticationPrincipal User user
    ) throws IOException {
        Post post = postService.createPost(new Post(textContent, image.getBytes(), date, user));
        return ResponseEntity.created(URI.create("/"+post.getId())).build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @AuthenticationPrincipal User user){
        postService.likePost(postId, user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId){
        Post post = postService.getPost(postId);
        return ResponseEntity.ok(new PostResponse(post));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts.stream().map(PostResponse::new).toList());
    }

}
