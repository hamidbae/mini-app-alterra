package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.dao.Collection;
import com.alterra.miniapp.domain.dao.Plant;
import com.alterra.miniapp.domain.dao.User;
import com.alterra.miniapp.domain.dto.CollectionDto;
import com.alterra.miniapp.domain.dto.PlantDto;
import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.repository.CollectionRepository;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.repository.UserRepository;
import com.alterra.miniapp.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CollectionService {
    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Object> addCollection(Long plantId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Plant> plant = plantRepository.findById(plantId);

        if(plant.isEmpty()){
            return Response.build("plant not found", null, null, HttpStatus.BAD_REQUEST);
        }

        Collection collection = Collection.builder()
                .plant(plant.get())
                .user(user.get())
                .build();

        collectionRepository.save(collection);

        CollectionDto collectionDto = CollectionDto.builder()
                .id(collection.getId())
                .createdAt(collection.getCreatedAt())
//                .user(UserDto.builder()
//                        .id(collection.getUser().getId())
//                        .name(collection.getUser().getName())
//                        .username(collection.getUser().getUsername())
//                        .build())
                .plant(PlantDto.builder()
                        .id(collection.getPlant().getId())
                        .name(collection.getPlant().getName())
                        .speciesName(collection.getPlant().getSpeciesName())
                        .build())
                .build();

        return Response.build(Response.create("collection"), collectionDto, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deleteCollection(Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Collection> collection = collectionRepository.findById(id);

        if(collection.isEmpty()){
            return Response.build("collection not found", null, null, HttpStatus.BAD_REQUEST);
        }

        if(!collection.get().getUser().getId().equals(user.get().getId())){
            return Response.build("not authorized", null, null, HttpStatus.UNAUTHORIZED);
        }

        collectionRepository.deleteById(id);

        return Response.build(Response.delete("collection"), null, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getUserCollection(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        List<Collection> collections = collectionRepository.findByUserId(user.get().getId());
        List<CollectionDto> collectionDtos = new ArrayList<>();

        collections.forEach(collection -> {
            CollectionDto collectionDto = CollectionDto.builder()
                    .id(collection.getId())
                    .plant(PlantDto.builder()
                            .id(collection.getPlant().getId())
                            .name(collection.getPlant().getName())
                            .speciesName(collection.getPlant().getSpeciesName())
                            .build())
                    .createdAt(collection.getCreatedAt())
                    .build();

            collectionDtos.add(collectionDto);
        });

        return Response.build(Response.get("collections"), collectionDtos, null, HttpStatus.OK);
    }

}
