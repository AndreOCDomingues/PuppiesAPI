package com.rethreadsinnovations.puppiesapi.service;

import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Long id);
    List<User> getUserByName(String name);
    User getUserByEmail(String email);
    Long createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    List<Post> getUserLikes(Long id);
    List<Post> getUserPosts(Long id);
}
