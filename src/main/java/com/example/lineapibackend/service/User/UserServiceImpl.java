package com.example.lineapibackend.service.User;


import com.example.lineapibackend.entity.User;
import com.example.lineapibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> createUser(User user) {
        return userRepository.insert(user);
    }
//    TODO: Test this method
    @Override
    public Mono<User> updateUser(User user, String id) {
        return userRepository.save(user);
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return userRepository.deleteById(id)
                .flatMap(user -> Mono.just(Boolean.TRUE));
    }
}
