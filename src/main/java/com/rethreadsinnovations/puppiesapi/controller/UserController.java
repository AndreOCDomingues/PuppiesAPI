package com.rethreadsinnovations.puppiesapi.controller;

import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.request.UserRequest;
import com.rethreadsinnovations.puppiesapi.model.response.PostResponse;
import com.rethreadsinnovations.puppiesapi.model.response.UserResponse;
import com.rethreadsinnovations.puppiesapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam(required = false) String name) {
        if (null != name && !name.isBlank())
            return ResponseEntity.ok(
                    userService.getUserByName(name)
                            .stream()
                            .map(UserResponse::new)
                            .toList()
            );

        return ResponseEntity.ok(
                userService.getAllUsers()
                        .stream()
                        .map(UserResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new UserResponse(
                        userService.getUserById(id)
                )
        );
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<PostResponse>> getUserLikes(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getUserLikes(id).stream()
                        .map(PostResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getUserPosts(id).stream()
                        .map(PostResponse::new)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserRequest userRequest) {
        Long id = userService.createUser(
                new User(userRequest)
        );

        return ResponseEntity.created(URI.create("/" + id)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(
                new UserResponse(
                        userService.updateUser(id, new User(userRequest))
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
