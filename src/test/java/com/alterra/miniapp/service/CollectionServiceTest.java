package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.common.ApiResponse;
import com.alterra.miniapp.domain.dao.*;
import com.alterra.miniapp.domain.dto.CollectionDto;
import com.alterra.miniapp.repository.CollectionRepository;
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
@SpringBootTest(classes = CollectionService.class)
public class CollectionServiceTest {


    @MockBean
    private CollectionRepository collectionRepository;

    @MockBean
    private PlantRepository plantRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    @Autowired
    private CollectionService collectionService;

    @Test
    void addCollectionSuccess_Test(){
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

        Collection collection = Collection.builder()
                .id(1L)
                .plant(plant)
                .user(user)
                .build();

        CustomUserDetails userDetails = CustomUserDetails.build(user);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(plantRepository.findById(1L)).thenReturn(Optional.ofNullable(plant));
        Mockito.when(userRepository.findByUsername("hamidb")).thenReturn(Optional.of(user));
        Mockito.when(collectionRepository.save(collection)).thenReturn(collection);

        ResponseEntity<Object> response = collectionService.addCollection(1L);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        CollectionDto data = (CollectionDto) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertNotNull(data);
        Assertions.assertEquals("hamidb", data.getUser().getUsername());
        Assertions.assertEquals("Corn", data.getPlant().getSpeciesName());
    }

    @Test
    void deleteCollectionSuccess_Test(){
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

        Collection collection = Collection.builder()
                .id(1L)
                .plant(plant)
                .user(user)
                .build();

        CustomUserDetails userDetails = CustomUserDetails.build(user);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        Mockito.when(userRepository.findByUsername("hamidb")).thenReturn(Optional.of(user));
        Mockito.when(collectionRepository.findById(1L)).thenReturn(Optional.ofNullable(collection));

        ResponseEntity<Object> response = collectionService.deleteCollection(1L);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        Assertions.assertNull(apiResponse.getData());
        Assertions.assertEquals("Delete collection success", apiResponse.getMessage());
    }
}
