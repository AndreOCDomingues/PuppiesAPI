package com.rethreadsinnovations.puppiesapi.controller;

import com.rethreadsinnovations.puppiesapi.exceptions.UserNotFoundException;
import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.request.AuthenticationResquest;
import com.rethreadsinnovations.puppiesapi.model.request.RegisterResquest;
import com.rethreadsinnovations.puppiesapi.model.response.AuthenticationResponse;
import com.rethreadsinnovations.puppiesapi.service.TokenService;
import com.rethreadsinnovations.puppiesapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationResquest request){
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterResquest request) {
        if(userAlreadyExists(request.email()))
            return ResponseEntity.badRequest().build();

        String encryptedPassword = passwordEncoder.encode(request.password());
        User newUser = new User(request.name(), request.email(), encryptedPassword, request.role());

        Long userId = userService.createUser(newUser);

        return ResponseEntity.created(URI.create("/"+userId)).build();
    }

    private boolean userAlreadyExists(String email) {
        try {
            userService.getUserByEmail(email);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

}
