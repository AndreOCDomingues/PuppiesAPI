package com.rethreadsinnovations.puppiesapi.model;

import com.rethreadsinnovations.puppiesapi.model.entities.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Serial
    private static final long serialVersionUID = 0;

    private Long id;
    private String textContent;
    private byte[] image;
    private LocalDate date;
    private User createdBy;
    private List<User> likedBy = new ArrayList<>();

    public Post(String textContent, byte[] image, LocalDate date, User createdBy) {
        this.textContent = textContent;
        this.image = image;
        this.date = date;
        this.createdBy = createdBy;
    }

    public Post(PostEntity postEntity){
        this.id = postEntity.getId();
        this.textContent = postEntity.getTextContent();
        this.image = postEntity.getImage();
        this.date = postEntity.getDate();
        this.createdBy = new User(postEntity.getCreatedBy());
        this.likedBy = postEntity.getLikedBy().stream().map(User::new).toList();
    }

}
