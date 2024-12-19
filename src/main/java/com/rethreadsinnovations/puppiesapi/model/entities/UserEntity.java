package com.rethreadsinnovations.puppiesapi.model.entities;

import com.rethreadsinnovations.puppiesapi.model.User;
import com.rethreadsinnovations.puppiesapi.model.security.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "puppies_users")
@Table(name = "puppies_users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puppies_user_seq")
    @SequenceGenerator(name = "puppies_user_seq", sequenceName = "puppies_user_sequence", initialValue = 2, allocationSize = 1)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PostEntity> posts = new ArrayList<>();

    @ManyToMany(mappedBy = "likedBy", fetch = FetchType.EAGER)
    private List<PostEntity> likedPosts = new ArrayList<>();

    public UserEntity(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

}