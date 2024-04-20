package com.ju.api.repository;

import com.ju.api.models.UserModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserRepository extends ReactiveCrudRepository<UserModel, Long> {
    Mono<UserModel> findByEmail(String email);
    Mono<UserModel> findByUsername(String username);
}
