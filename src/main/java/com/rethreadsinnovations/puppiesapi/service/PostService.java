package com.rethreadsinnovations.puppiesapi.service;

import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post createPost(Post post) throws IOException;

    void likePost(Long postId, User user);

    Post getPost(Long postId);

    List<Post> getAllPosts();
}
