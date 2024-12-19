package com.rethreadsinnovations.puppiesapi.repository;

import com.rethreadsinnovations.puppiesapi.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<List<UserEntity>> findByName(String name);

}
