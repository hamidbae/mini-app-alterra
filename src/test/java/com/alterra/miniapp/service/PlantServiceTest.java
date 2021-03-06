package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.common.ApiResponse;
import com.alterra.miniapp.domain.dao.*;
import com.alterra.miniapp.domain.dto.PlantDto;
import com.alterra.miniapp.repository.CommentRepository;
import com.alterra.miniapp.repository.FavouriteRepository;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.h2.table.Plan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PlantService.class)
public class PlantServiceTest {

    @MockBean
    private PlantRepository plantRepository;

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    FavouriteRepository favouriteRepository;

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

        Mockito.when(plantRepository.findAllSorted()).thenReturn(plants);

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

    @Test
    void getPlantsDetailSuccess_Test(){
        Plant plant = Plant.builder().id(1L).name("Jagung").speciesName("Corn").content("Jagung enak sekali").build();
        User user = User.builder().id(1L).username("hamidb").name("Hamid").password("password").build();
        Comment comment = Comment.builder().id(1L).text("I have this plant").user(user).plant(plant).build();
        Favourite favourite = Favourite.builder().id(1L).user(user).plant(plant).build();

        List<Plant> plants = new ArrayList<>();
        plants.add(plant);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Set<Favourite> favourites = new HashSet<>();
        favourites.add(favourite);


        Mockito.when(plantRepository.findAllSorted()).thenReturn(plants);
        Mockito.when(commentRepository.searchByPlantId(1L)).thenReturn(comments);
        Mockito.when(favouriteRepository.searchByPlantId(1L)).thenReturn(favourites);

        ResponseEntity<Object> response = plantService.getPlantsDetail();

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        List<PlantDto> data = (List<PlantDto>) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals(1, data.size());
    }

    @Test
    void searchPlantsSuccess_Test(){
        Plant plant = Plant.builder().id(1L).name("Jagung").speciesName("Corn").content("Jagung enak sekali").build();
        User user = User.builder().id(1L).username("hamidb").name("Hamid").password("password").build();
        Comment comment = Comment.builder().id(1L).text("I have this plant").user(user).plant(plant).build();
        Favourite favourite = Favourite.builder().id(1L).user(user).plant(plant).build();

        List<Plant> plants = new ArrayList<>();
        plants.add(plant);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Set<Favourite> favourites = new HashSet<>();
        favourites.add(favourite);


        Mockito.when(plantRepository.searchPlant("gung")).thenReturn(plants);
        Mockito.when(commentRepository.searchByPlantId(1L)).thenReturn(comments);
        Mockito.when(favouriteRepository.searchByPlantId(1L)).thenReturn(favourites);

        ResponseEntity<Object> response = plantService.searchPlants("gung");

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        List<PlantDto> data = (List<PlantDto>) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals(1, data.size());
    }

}
