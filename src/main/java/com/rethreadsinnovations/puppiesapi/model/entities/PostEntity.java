package com.rethreadsinnovations.puppiesapi.model.entities;

import com.rethreadsinnovations.puppiesapi.model.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "posts")
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String textContent;

    @Lob
    private byte[] image;

    private LocalDate date;

    @ManyToOne
    private UserEntity createdBy;

    @ManyToMany
    private List<UserEntity> likedBy = new ArrayList<>();

    public PostEntity(Post post) {
        this.textContent = post.getTextContent();
        this.image = post.getImage();
        this.date = post.getDate();
        this.createdBy = new UserEntity(post.getCreatedBy());
    }

}
