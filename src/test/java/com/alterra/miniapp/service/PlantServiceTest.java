package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.common.ApiResponse;
import com.alterra.miniapp.domain.dao.ERole;
import com.alterra.miniapp.domain.dao.Plant;
import com.alterra.miniapp.domain.dao.Role;
import com.alterra.miniapp.domain.dto.PlantDto;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.h2.table.Plan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PlantService.class)
public class PlantServiceTest {

    @MockBean
    private PlantRepository plantRepository;

    @Autowired
    private PlantService plantService;

    @Test
    void getAllPlantSuccess_Test(){
        Plant plant = Plant.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();

        List<Plant> plants = new ArrayList<>();
        plants.add(plant);

        Mockito.when(plantRepository.findAll()).thenReturn(plants);

        ResponseEntity<Object> response = plantService.getAllPlants();

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        List<PlantDto> data = (List<PlantDto>) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals(1, data.size());
    }


    @Test
    void getOnePlantSuccess_Test(){
        Plant plant = Plant.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();


        Mockito.when(plantRepository.findById(1L)).thenReturn(Optional.ofNullable(plant));

        ResponseEntity<Object> response = plantService.getOnePlant(1L);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        PlantDto data = (PlantDto) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals("Jagung enak sekali", data.getContent());
    }

    @Test
    void createPlantSuccess_Test(){
        Plant plant = Plant.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();

        PlantDto plantDto = PlantDto.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();

        Mockito.when(plantRepository.save(plant)).thenReturn(plant);

        ResponseEntity<Object> response = plantService.addPlant(plantDto);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        PlantDto data = (PlantDto) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals("Jagung", data.getName());
    }

    @Test
    void deletePlantSuccess_Test(){
        Plant plant = Plant.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();

        Mockito.when(plantRepository.findById(1L)).thenReturn(Optional.ofNullable(plant));

        ResponseEntity<Object> response = plantService.deletePlant(1L);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        Object data = Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals(null, data);
        Assertions.assertEquals("Delete plant success", apiResponse.getMessage());
    }


    @Test
    void updatePlantSuccess_Test(){
        Plant plant = Plant.builder()
                .id(1L)
                .name("Jagung")
                .speciesName("Corn")
                .content("Jagung enak sekali")
                .build();

        PlantDto plantDto = PlantDto.builder()
                .name("Jagung Keren")
                .speciesName("Cornn")
                .content("Jagung enak banget")
                .build();

        Mockito.when(plantRepository.findById(1L)).thenReturn(Optional.ofNullable(plant));

        ResponseEntity<Object> response = plantService.updatePlant(1L, plantDto);

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        PlantDto data = (PlantDto) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals("Cornn", data.getSpeciesName());
    }

}
