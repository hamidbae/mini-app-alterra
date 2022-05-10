package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.dao.Plant;
import com.alterra.miniapp.domain.dto.PlantDto;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {
    @Autowired
    PlantRepository plantRepository;

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

        return Response.build(Response.update("plant"), plant, null, HttpStatus.CREATED);
    }
}
