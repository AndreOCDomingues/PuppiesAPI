package com.rethreadsinnovations.puppiesapi.model;

import com.rethreadsinnovations.puppiesapi.model.entities.UserEntity;
import com.rethreadsinnovations.puppiesapi.model.request.UserRequest;
import com.rethreadsinnovations.puppiesapi.model.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private List<Post> posts = new ArrayList<>();
    private List<Post> likedPosts = new ArrayList<>();

    public User(UserEntity userEntity){
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.role = userEntity.getRole();
    }

    public User(UserRequest userRequest){
        this.name = userRequest.getName();
        this.email = userRequest.getEmail();
    }

    public User(String name, String email, String password, UserRole role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(UserRole.ADMIN.equals(this.role))
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
