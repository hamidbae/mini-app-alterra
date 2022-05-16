package com.alterra.miniapp.service;

import com.alterra.miniapp.config.security.jwt.JwtUtils;
import com.alterra.miniapp.domain.common.ApiResponse;
import com.alterra.miniapp.domain.dao.ERole;
import com.alterra.miniapp.domain.dao.Role;
import com.alterra.miniapp.domain.dao.User;
import com.alterra.miniapp.domain.dto.CommentDto;
import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.repository.CommentRepository;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.repository.RoleRepository;
import com.alterra.miniapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserService.class)
@Slf4j
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

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

    @Test
    void registerUserNameExist_Test(){
        Role userRole = Role.builder().id(1L).name(ERole.USER).build();
        Role adminRole = Role.builder().id(1L).name(ERole.ADMIN).build();

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);
        User user1 = User.builder().id(1L).username("hamidb").name("Hamid").password("password").roles(roles).build();
        User user2 = User.builder().id(1L).username("hamidb").name("Hamid").password("password").roles(roles).build();

        Set<String> rolesDto = new HashSet<>();
        rolesDto.add("user");
        rolesDto.add("admin");
        UserDto userDto = UserDto.builder().id(1L).username("hamidb").name("Hamid").password("password").roles(rolesDto).build();

        Mockito.when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        ResponseEntity<Object> response = userService.registerUser(userDto);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        Assertions.assertNull(apiResponse.getData());
        Assertions.assertEquals(String.format("user with username %s exist", userDto.getUsername()), apiResponse.getMessage());
    }

//    @Test
//    void loginUserSuccess_Test(){
//
//        Role userRole = Role.builder().id(1L).name(ERole.USER).build();
//        Role adminRole = Role.builder().id(1L).name(ERole.ADMIN).build();
//
//        Set<Role> roles = new HashSet<>();
//        roles.add(userRole);
//        roles.add(adminRole);
//        User user = User.builder().id(1L).username("hamidb").name("Hamid").password(encoder.encode("password")).roles(roles).build();
//        log.info(encoder.encode("password"));
//        UserDto userDto = UserDto.builder().username("hamidb").password("password").build();
//
//        Mockito.when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);
//        Mockito.when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.ofNullable(user));
//
//        Boolean isPasswordCorrect = encoder.matches(userDto.getPassword(), user.getPassword());
//        log.info(isPasswordCorrect.toString());
//
//        ResponseEntity<Object> response = userService.loginUser(userDto);
//
//        ApiResponse apiResponse = (ApiResponse) response.getBody();
//        log.info(apiResponse.getCode());
//        log.info(apiResponse.getMessage());
//
//        Map<String, String> data = (Map<String, String>) Objects.requireNonNull(apiResponse).getData();
//        data.forEach((el1, el2) -> {
//            Assertions.assertNotNull(el1);
//            Assertions.assertNotNull(el2);
//        });
//    }

    @Test
    void updateUserSuccess_Test(){

        Role userRole = Role.builder().id(1L).name(ERole.USER).build();
        Role adminRole = Role.builder().id(1L).name(ERole.ADMIN).build();

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);

        User user = User.builder().id(1L).username("hamidb").name("Hamid").password(encoder.encode("password")).roles(roles).build();
        UserDto userDto = UserDto.builder().name("Hamid Baehaqi").build();

        CustomUserDetails userDetails = CustomUserDetails.build(user);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userRepository.findByUsername("hamidb")).thenReturn(Optional.ofNullable(user));

        ResponseEntity<Object> response = userService.updateUser(userDto);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        log.info(apiResponse.getCode());
        log.info(apiResponse.getMessage());
        UserDto data = (UserDto) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals("Hamid Baehaqi", data.getName());
        Assertions.assertEquals("Update user success", apiResponse.getMessage());

    }

    @Test
    void deleteUserSuccess_Test(){

        Role userRole = Role.builder().id(1L).name(ERole.USER).build();
        Role adminRole = Role.builder().id(1L).name(ERole.ADMIN).build();

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);

        User user = User.builder().id(1L).username("hamidb").name("Hamid").password(encoder.encode("password")).roles(roles).build();

        CustomUserDetails userDetails = CustomUserDetails.build(user);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userRepository.findByUsername("hamidb")).thenReturn(Optional.ofNullable(user));

        ResponseEntity<Object> response = userService.deleteUser();

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        Assertions.assertNull(apiResponse.getData());
        Assertions.assertEquals("Delete user success", apiResponse.getMessage());

    }


}
