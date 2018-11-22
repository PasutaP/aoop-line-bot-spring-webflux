package com.example.lineapibackend.service.User;

import com.example.lineapibackend.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(User user);

    Mono<User> updateUser(User user, String id);

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<Boolean> delete(String id);
}
