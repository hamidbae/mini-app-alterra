package com.alterra.miniapp.service;

import com.alterra.miniapp.config.security.jwt.JwtUtils;
import com.alterra.miniapp.domain.common.ApiResponse;
import com.alterra.miniapp.domain.dao.ERole;
import com.alterra.miniapp.domain.dao.Role;
import com.alterra.miniapp.domain.dao.User;
import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.repository.RoleRepository;
import com.alterra.miniapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserService.class)
@Slf4j
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void registerUserSuccess_Test(){
        Role userRole = Role.builder().id(1L).name(ERole.USER).build();
        Role adminRole = Role.builder().id(1L).name(ERole.ADMIN).build();

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user1 = User.builder().id(1L).username("hamidb").name("Hamid").password("password").roles(roles).build();

        Set<String> rolesDto = new HashSet<>();
        rolesDto.add("user");
        UserDto userDto1 = UserDto.builder().id(1L).username("hamidb").name("Hamid").password("password").roles(rolesDto).build();

        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        Mockito.when(roleRepository.findByName(ERole.ADMIN)).thenReturn(Optional.ofNullable(adminRole));
        Mockito.when(roleRepository.findByName(ERole.USER)).thenReturn(Optional.ofNullable(userRole));

        ResponseEntity<Object> response = userService.registerUser(userDto1);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        UserDto data = (UserDto) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals("Hamid", data.getName());
    }

//    @Test
//    void loginUserSuccess_Test(){
//        Role userRole = Role.builder().id(1L).name(ERole.USER).build();
////        Role adminRole = Role.builder().id(1L).name(ERole.ADMIN).build();
//
//        Set<Role> roles = new HashSet<>();
//        roles.add(userRole);
//        User user = User.builder().id(1L).username("hamidb").name("Hamid").password(encoder.encode("password")).roles(roles).build();
//        log.info(user.getPassword());
//
////        Set<String> rolesDto = new HashSet<>();
////        rolesDto.add("user");
//        UserDto userDto = UserDto.builder().username("hamidb").password("password").build();
//
//        Mockito.when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);
//        Mockito.when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.ofNullable(user));
//
//        ResponseEntity<Object> response = userService.loginUser(userDto);
//        log.info(response.toString());
//        ApiResponse apiResponse = (ApiResponse) response.getBody();
//        Map<String, String> data = (Map<String, String>) Objects.requireNonNull(apiResponse).getData();
//        Assertions.assertNotNull(data);
//    }

}
