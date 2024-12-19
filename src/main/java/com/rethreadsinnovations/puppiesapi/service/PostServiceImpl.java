package com.rethreadsinnovations.puppiesapi.service;

import com.rethreadsinnovations.puppiesapi.exceptions.PostNotFoundException;
import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.entities.PostEntity;
import com.rethreadsinnovations.puppiesapi.model.entities.UserEntity;
import com.rethreadsinnovations.puppiesapi.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(Post post) throws IOException {
        PostEntity posted = postRepository.save(new PostEntity(post));

        return new Post(posted);
    }

    @Override
    public void likePost(Long postId, User user) {
        PostEntity postEntity = retrivePost(postId);
        postEntity.getLikedBy().add(new UserEntity(user));
        postRepository.save(postEntity);
    }

    @Override
    public Post getPost(Long postId) {
        return new Post(retrivePost(postId));

    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll().stream()
                .sorted(Comparator.comparing(PostEntity::getDate).reversed())
                .map(Post::new)
                .toList();
    }

    private PostEntity retrivePost(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id '" + postId + "' not found"));
    }
}
