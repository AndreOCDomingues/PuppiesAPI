package com.rethreadsinnovations.puppiesapi.service;

import com.rethreadsinnovations.puppiesapi.exceptions.UserNotFoundException;
import com.rethreadsinnovations.puppiesapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorizationServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthorizationService authorizationService;

    private User user;
    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail(email);
        user.setPassword("password123");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWhenUserExists() {
        when(userService.getUserByEmail(email)).thenReturn(user);

        UserDetails result = authorizationService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
        verify(userService).getUserByEmail(email);
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenUserNotFound() {
        when(userService.getUserByEmail(email)).thenThrow(new UserNotFoundException("User with email '" + email + "' not found"));

        assertThrows(UserNotFoundException.class, () -> authorizationService.loadUserByUsername(email));
        verify(userService).getUserByEmail(email);
    }
}
