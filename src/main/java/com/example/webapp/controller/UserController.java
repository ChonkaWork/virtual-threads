package com.example.webapp.controller;

import com.example.webapp.domain.User;
import com.example.webapp.exception.UserNotFoundException;
import com.example.webapp.repository.UserRepository;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping
    @SneakyThrows
    public List<User> getAllUsers(){
        Thread.sleep(1000);
        log.info("Thread info {}", Thread.currentThread());
        return userRepository.findAll();
    }

}
