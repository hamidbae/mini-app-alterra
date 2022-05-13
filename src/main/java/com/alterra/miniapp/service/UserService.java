package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.dao.ERole;
import com.alterra.miniapp.domain.dao.Role;
import com.alterra.miniapp.domain.dao.User;
import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.repository.RoleRepository;
import com.alterra.miniapp.repository.UserRepository;
import com.alterra.miniapp.config.security.jwt.JwtUtils;
import com.alterra.miniapp.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    public ResponseEntity<Object> registerUser(UserDto userDto) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userDto.getUsername()))) {
            return Response.build(Response.exist("user", "username", userDto.getUsername()), null, null, HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .password(encoder.encode(userDto.getPassword()))
                .build();

        Set<String> strRoles = userDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.USER).orElseThrow(() ->
                    new RuntimeException("Error: Role is not found.")
            );
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ADMIN).orElseThrow(() ->
                            new RuntimeException("Error: Role is not found.")
                    );
                    roles.add(adminRole);
                }
                if ("user".equals(role)) {
                    Role userRole = roleRepository.findByName(ERole.USER).orElseThrow(() ->
                            new RuntimeException("Error: Role is not found.")
                    );
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        UserDto userNoPasswordDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .build();

        Set<String> roleDto = new HashSet<>();
        user.getRoles().forEach(role -> roleDto.add(role.getName().name()));
        userNoPasswordDto.setRoles(roleDto);

        return Response.build(Response.add("User"), userNoPasswordDto, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> loginUser(UserDto userDto) {
        if (Boolean.FALSE.equals(userRepository.existsByUsername(userDto.getUsername()))) {
            return Response.build("username or password incorrect", null, null, HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findByUsername(userDto.getUsername());
        Boolean isPasswordCorrect = encoder.matches(userDto.getPassword(), user.get().getPassword());
        if (Boolean.FALSE.equals(isPasswordCorrect)) {
            return Response.build("username or password incorrect", null, null, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        Map<String, String> token = new HashMap<>();
        token.put("token", jwt);

        return Response.build("Login success", token, null, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateUser(UserDto userDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);

        if (!Objects.equals(userDto.getName(), "") && userDto.getName() != null) {
            user.get().setName(userDto.getName());
        }

        if (!Objects.equals(userDto.getUsername(), "") && userDto.getUsername() != null) {
            if (Boolean.TRUE.equals(userRepository.existsByUsername(userDto.getUsername()))) {
                return Response.build(Response.exist("user", "username", userDto.getUsername()), null, null, HttpStatus.BAD_REQUEST);
            }
            user.get().setUsername(userDto.getUsername());
        }

        if (!Objects.equals(userDto.getPassword(), "") && userDto.getPassword() != null) {
            user.get().setPassword(encoder.encode(userDto.getPassword()));
        }

        if (!Objects.equals(userDto.getRoles(), new HashSet<>()) && userDto.getRoles() != null) {
            Set<String> strRoles = userDto.getRoles();
            Set<Role> roles = new HashSet<>();

            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ADMIN).orElseThrow(() ->
                            new RuntimeException("Error: Role is not found.")
                    );
                    roles.add(adminRole);
                }
                if ("user".equals(role)) {
                    Role userRole = roleRepository.findByName(ERole.USER).orElseThrow(() ->
                            new RuntimeException("Error: Role is not found.")
                    );
                    roles.add(userRole);
                }
            });

            user.get().setRoles(roles);
        }

        userRepository.save(user.get());

        UserDto userDto1 = UserDto.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .username(user.get().getUsername())
                .build();

        Set<String> roleDto = new HashSet<>();
        user.get().getRoles().forEach(role -> roleDto.add(role.getName().name()));
        userDto1.setRoles(roleDto);

        return Response.build(Response.update("user"), userDto1, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deleteUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);

        userRepository.deleteById(user.get().getId());

        return Response.build(Response.delete("user"), null, null, HttpStatus.CREATED);
    }
}