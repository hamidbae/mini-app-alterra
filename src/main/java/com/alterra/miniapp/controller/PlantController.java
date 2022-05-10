package com.alterra.miniapp.controller;

import com.alterra.miniapp.domain.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
public class PlantController {

    @GetMapping("/plants")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>("get all plant", HttpStatus.OK);
    }

    @PostMapping("/plants")
    public ResponseEntity<Object> add() {
        return new ResponseEntity<>("add plant", HttpStatus.CREATED);
    }

}
