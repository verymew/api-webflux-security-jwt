package com.ju.api.repository;

import com.ju.api.models.UserModel;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
@Repository
public interface UserRepository extends ReactiveCrudRepository<UserModel, Long> {
    Mono<UserModel> findByEmail(String email);
    Mono<UserModel> findByUsername(String username);
}
