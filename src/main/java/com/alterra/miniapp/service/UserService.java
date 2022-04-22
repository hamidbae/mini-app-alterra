package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.dao.User;
import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Object> getAllUser(){
        HashMap<String, List<UserDto>> userMap = new HashMap<>();
        List<UserDto> dtoList = new ArrayList<>();
        List<User> users = userRepository.findAll();

        users.forEach((user) -> {
            dtoList.add(UserDto.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .build());
        });

        userMap.put("data", dtoList);

        return new ResponseEntity<>(userMap, HttpStatus.OK);
    }
}
