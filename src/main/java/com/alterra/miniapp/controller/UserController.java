package com.alterra.miniapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/helow")
    public ResponseEntity<Object> helow(){
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }
}
