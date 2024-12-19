package com.rethreadsinnovations.puppiesapi.repository;

import com.rethreadsinnovations.puppiesapi.model.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
