package com.rethreadsinnovations.puppiesapi.model.request;

import com.rethreadsinnovations.puppiesapi.model.security.UserRole;

public record RegisterResquest(String name, String email, String password, UserRole role) {
}
