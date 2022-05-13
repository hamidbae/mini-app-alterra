package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.common.ApiResponse;
import com.alterra.miniapp.domain.dao.*;
import com.alterra.miniapp.domain.dto.CommentDto;
import com.alterra.miniapp.repository.CommentRepository;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CommentService.class)
public class CommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private PlantRepository plantRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    @Autowired
    private CommentService commentService;

    @Test
    void addCommentSuccess_Test(){
        Plant plant = Plant.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();

        Role userRole = Role.builder().id(1L).name(ERole.USER).build();

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user = User.builder().id(1L).username("hamidb").name("Hamid").password("password").roles(roles).build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Bagus")
                .plant(plant)
                .user(user)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .text("Bagus")
                .build();

        CustomUserDetails userDetails = CustomUserDetails.build(user);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(plantRepository.findById(1L)).thenReturn(Optional.ofNullable(plant));
        Mockito.when(userRepository.findByUsername("hamidb")).thenReturn(Optional.of(user));
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        ResponseEntity<Object> response = commentService.addComment(1L, commentDto);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        CommentDto data = (CommentDto) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals("Bagus", data.getText());
    }

    @Test
    void updateCommentSuccess_Test(){
        Plant plant = Plant.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();

        Role userRole = Role.builder().id(1L).name(ERole.USER).build();

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user = User.builder().id(1L).username("hamidb").name("Hamid").password("password").roles(roles).build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Bagus")
                .plant(plant)
                .user(user)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .text("Bagus Sekali")
                .build();

        CustomUserDetails userDetails = CustomUserDetails.build(user);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(userRepository.findByUsername("hamidb")).thenReturn(Optional.of(user));
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.ofNullable(comment));

        ResponseEntity<Object> response = commentService.updateComment(1L, commentDto);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        CommentDto data = (CommentDto) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals("Bagus Sekali", data.getText());
    }

    @Test
    void deleteCommentSuccess_Test(){
        Plant plant = Plant.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();

        Role userRole = Role.builder().id(1L).name(ERole.USER).build();
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user = User.builder().id(1L).username("hamidb").name("Hamid").password("password").roles(roles).build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Bagus")
                .plant(plant)
                .user(user)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .text("Bagus")
                .build();

        CustomUserDetails userDetails = CustomUserDetails.build(user);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(userRepository.findByUsername("hamidb")).thenReturn(Optional.of(user));
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.ofNullable(comment));

        ResponseEntity<Object> response = commentService.deleteComment(1L);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        Assertions.assertNull(apiResponse.getData());
        Assertions.assertEquals("Delete comment success", apiResponse.getMessage());
    }
}
