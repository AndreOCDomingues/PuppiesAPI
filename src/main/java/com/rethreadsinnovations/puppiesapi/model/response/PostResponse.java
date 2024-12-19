package com.rethreadsinnovations.puppiesapi.model.response;

import com.rethreadsinnovations.puppiesapi.model.Post;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PostResponse {

    private Long id;
    private String textContent;
    private LocalDate date;
    private UserResponse createdBy;
    private List<UserResponse> likedBy;
    private byte[] image;

    public PostResponse(Post post){
        this.id = post.getId();
        this.textContent = post.getTextContent();
        this.image = post.getImage();
        this.date = post.getDate();
        this.createdBy = new UserResponse(post.getCreatedBy());
        this.likedBy = post.getLikedBy().stream().map(UserResponse::new).toList();
    }

}
