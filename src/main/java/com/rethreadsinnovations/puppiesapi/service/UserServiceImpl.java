package com.rethreadsinnovations.puppiesapi.service;

import com.rethreadsinnovations.puppiesapi.exceptions.UserNotFoundException;
import com.rethreadsinnovations.puppiesapi.model.Post;
import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.entities.UserEntity;
import com.rethreadsinnovations.puppiesapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(User::new)
                .toList();
    }

    @Override
    public User getUserById(Long id) {
        return new User(
                getUserEntity(id)
        );
    }

    @Override
    public List<User> getUserByName(String name) {
        List<UserEntity> userEntityList = userRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException("User with name '" + name + "' not found"));

        return userEntityList.stream()
                .map(User::new)
                .toList();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::new)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + email + "' not found"));
    }

    @Override
    public Long createUser(User user) {
        UserEntity userEntitySaved = userRepository.save(new UserEntity(user));
        return userEntitySaved.getId();
    }

    @Override
    public User updateUser(Long id, User user) {
        UserEntity userEntity = getUserEntity(id);

        if (null != user.getName())
            userEntity.setName(user.getName());
        if (null != user.getEmail())
            userEntity.setEmail(user.getEmail());

        return new User(
                userRepository.save(userEntity)
        );
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(
                getUserEntity(id)
        );
    }

    @Override
    public List<Post> getUserLikes(Long id) {
        return getUserEntity(id)
                .getLikedPosts()
                .stream()
                .map(Post::new)
                .toList();
    }

    @Override
    public List<Post> getUserPosts(Long id) {
        return getUserEntity(id)
                .getPosts()
                .stream()
                .map(Post::new)
                .toList();
    }

    private UserEntity getUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' not found"));
    }

}
