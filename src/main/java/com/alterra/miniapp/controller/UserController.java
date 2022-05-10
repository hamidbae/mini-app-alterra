package com.alterra.miniapp.controller;

import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<Object> register(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PostMapping("/users/login")
    public ResponseEntity<Object> login(@RequestBody UserDto userDto) {
        return userService.loginUser(userDto);
    }

    @PutMapping("/users")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Object> delete() {
        return userService.deleteUser();
    }
}
