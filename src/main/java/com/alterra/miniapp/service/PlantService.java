package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.dao.Comment;
import com.alterra.miniapp.domain.dao.Favourite;
import com.alterra.miniapp.domain.dao.Plant;
import com.alterra.miniapp.domain.dto.CommentDto;
import com.alterra.miniapp.domain.dto.FavouriteDto;
import com.alterra.miniapp.domain.dto.PlantDto;
import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.repository.CommentRepository;
import com.alterra.miniapp.repository.FavouriteRepository;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PlantService {
    @Autowired
    PlantRepository plantRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FavouriteRepository favouriteRepository;

    public ResponseEntity<Object> getAllPlants(){
        List<Plant> daoList = plantRepository.findAll();
        List<PlantDto> dtoList = new ArrayList<>();

        for(Plant dao:daoList){
            dtoList.add(PlantDto.builder()
                    .id(dao.getId())
                    .name(dao.getName())
                    .speciesName(dao.getSpeciesName())
                    .content(dao.getContent())
                    .createdAt(dao.getCreatedAt())
                    .updatedAt(dao.getUpdatedAt())
                    .build());
        }

        return Response.build(Response.get("plants"), dtoList, null, HttpStatus.OK);
    }

    public ResponseEntity<Object> getOnePlant(Long id){
        Optional<Plant> plant = plantRepository.findById(id);

        if(plant.isEmpty()){
            return Response.build("plant not found", null, null, HttpStatus.BAD_REQUEST);
        }

        PlantDto plantDto = PlantDto.builder()
                .id(plant.get().getId())
                .name(plant.get().getName())
                .speciesName(plant.get().getSpeciesName())
                .content(plant.get().getContent())
                .createdAt(plant.get().getCreatedAt())
                .updatedAt(plant.get().getUpdatedAt())
                .build();

        return Response.build(Response.get("plant"), plantDto, null, HttpStatus.OK);
    }

    public ResponseEntity<Object> addPlant(PlantDto requestBody){
        Plant plant = Plant.builder()
                .id(requestBody.getId())
                .name(requestBody.getName())
                .speciesName(requestBody.getSpeciesName())
                .content(requestBody.getContent()).build();

        plantRepository.save(plant);

        PlantDto plantDto = PlantDto.builder()
                .id(plant.getId())
                .name(plant.getName())
                .speciesName(plant.getSpeciesName())
                .content(plant.getContent())
                .updatedAt(plant.getUpdatedAt())
                .createdAt(plant.getCreatedAt())
                .build();

        return Response.build(Response.create("plant"), plantDto, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deletePlant(Long id){
        Optional<Plant> plant = plantRepository.findById(id);
        if(plant.isEmpty()){
            return Response.build("plant not found", null, null, HttpStatus.BAD_REQUEST);
        }

        plantRepository.deleteById(id);

        return Response.build(Response.delete("plant"), null, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updatePlant(Long id, PlantDto requestBody){
        Optional<Plant> plant = plantRepository.findById(id);
        if(plant.isEmpty()){
            return Response.build("plant not found", null, null, HttpStatus.BAD_REQUEST);
        }

        if(!requestBody.getName().isBlank()){
            plant.get().setName(requestBody.getName());
        }

        if(!requestBody.getSpeciesName().isBlank()){
            plant.get().setSpeciesName(requestBody.getSpeciesName());
        }

        if(!requestBody.getContent().isBlank()){
            plant.get().setContent(requestBody.getContent());
        }

        plantRepository.save(plant.get());

        PlantDto plantDto = PlantDto.builder()
                .id(plant.get().getId())
                .name(plant.get().getName())
                .speciesName(plant.get().getSpeciesName())
                .content(plant.get().getContent())
                .createdAt(plant.get().getCreatedAt())
                .updatedAt(plant.get().getUpdatedAt())
                .build();

        return Response.build(Response.update("plant"), plantDto, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getPlantsDetail(){
        List<Plant> daoList = plantRepository.findAll();
        List<PlantDto> dtoList = new ArrayList<>();

        daoList.forEach(plant -> {
            PlantDto plantDto = PlantDto.builder()
                    .id(plant.getId())
                    .name(plant.getName())
                    .speciesName(plant.getSpeciesName())
                    .content(plant.getContent())
                    .createdAt(plant.getCreatedAt())
                    .updatedAt(plant.getUpdatedAt())
                    .build();

            List<CommentDto> commentDtos = new ArrayList<>();
            plant.getComments().forEach(comment -> {
//                log.info(comment.getText());
                CommentDto commentDto = CommentDto.builder()
                        .createdAt(comment.getCreatedAt())
                        .id(comment.getId())
                        .text(comment.getText())
                        .user(UserDto.builder()
                                .id(comment.getUser().getId())
                                .name(comment.getUser().getName())
                                .username(comment.getUser().getUsername())
                                .build())
                        .build();
                commentDtos.add(commentDto);
            });

            plantDto.setComments(commentDtos);

            Set<FavouriteDto> favouriteDtos = new HashSet<>();
            plant.getFavourites().forEach(favourite -> {
//                log.info(favourite.getId().toString());
                FavouriteDto favouriteDto = FavouriteDto.builder()
                        .id(favourite.getId())
                        .user(UserDto.builder()
                                .id(favourite.getUser().getId())
                                .name(favourite.getUser().getName())
                                .username(favourite.getUser().getUsername())
                                .build())
                        .build();

                favouriteDtos.add(favouriteDto);
            });

            plantDto.setFavourites(favouriteDtos);
            plantDto.setTotalFavourite(plant.getFavourites().size());
            dtoList.add(plantDto);
        });

        return Response.build(Response.get("plants"), dtoList, null, HttpStatus.OK);
    }

    public ResponseEntity<Object> searchPlants(String keyword){
        List<Plant> daoList = plantRepository.searchPlant(keyword);
        List<PlantDto> dtoList = new ArrayList<>();

        daoList.forEach(plant -> {
            PlantDto plantDto = PlantDto.builder()
                    .id(plant.getId())
                    .name(plant.getName())
                    .speciesName(plant.getSpeciesName())
                    .content(plant.getContent())
                    .createdAt(plant.getCreatedAt())
                    .updatedAt(plant.getUpdatedAt())
                    .build();

            List<Comment> comments = commentRepository.searchByPlantId(plant.getId());
            List<CommentDto> commentDtos = new ArrayList<>();
            log.info("plant id = {}", plant.getId().toString());

            comments.forEach(comment -> {
                log.info("comment id = {}", comment.getId().toString());
                CommentDto commentDto = CommentDto.builder()
                        .createdAt(comment.getCreatedAt())
                        .id(comment.getId())
                        .text(comment.getText())
                        .user(UserDto.builder()
                                .id(comment.getUser().getId())
                                .name(comment.getUser().getName())
                                .username(comment.getUser().getUsername())
                                .build())
                        .build();
                commentDtos.add(commentDto);
            });

            plantDto.setComments(commentDtos);

            Set<Favourite> favourites = favouriteRepository.searchByPlantId(plant.getId());
            Set<FavouriteDto> favouriteDtos = new HashSet<>();

            favourites.forEach(favourite -> {
                FavouriteDto favouriteDto = FavouriteDto.builder()
                        .id(favourite.getId())
                        .user(UserDto.builder()
                                .id(favourite.getUser().getId())
                                .name(favourite.getUser().getName())
                                .username(favourite.getUser().getUsername())
                                .build())
                        .build();

                favouriteDtos.add(favouriteDto);
            });

            plantDto.setFavourites(favouriteDtos);
            plantDto.setTotalFavourite(plant.getFavourites().size());
            dtoList.add(plantDto);
        });

        return Response.build("Search plants success", dtoList, null, HttpStatus.OK);
    }
}
