package com.example.lineapibackend.controller;


import com.example.lineapibackend.entity.User;
import com.example.lineapibackend.service.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/user")
public class UserController {
    private static final String TAG = "UserController";
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> findAll() {
        logger.debug(TAG + ": get all users");
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<User> findUserById(@PathVariable String id) {
        logger.debug(TAG + ": Find user by Id - " + id);
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@RequestBody User user) {
        logger.debug(TAG + ": Create a user");
        return userService.createUser(user);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Boolean> delete(@PathVariable String id) {
        logger.debug(TAG + ": Delete user with Id - " + id);
        return userService.delete(id);
    }

    @PutMapping(value = "/{id}")
    public Mono<User> updateUser(@RequestBody User user, @PathVariable String id) {
        return userService.updateUser(user, id);
    }


}
