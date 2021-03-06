package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.dao.*;
import com.alterra.miniapp.domain.dto.CommentDto;
import com.alterra.miniapp.domain.dto.FavouriteDto;
import com.alterra.miniapp.domain.dto.PlantDto;
import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.repository.FavouriteRepository;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.repository.UserRepository;
import com.alterra.miniapp.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavouriteService {
    @Autowired
    FavouriteRepository favouriteRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Object> addFavourite(Long plantId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Plant> plant = plantRepository.findById(plantId);
        if(plant.isEmpty()){
            return Response.build("plant not found", null, null, HttpStatus.BAD_REQUEST);
        }

        List<Favourite> favourites = favouriteRepository.findByUserIdPlantId(user.get().getId(), plantId);
        if(!favourites.isEmpty()){
            return Response.build("plant already in favourites", null, null, HttpStatus.BAD_REQUEST);
        }

        Favourite favourite = Favourite.builder()
                .plant(plant.get())
                .user(user.get())
                .build();

        favouriteRepository.save(favourite);

        FavouriteDto favouriteDto = FavouriteDto.builder()
                .id(favourite.getId())
                .user(UserDto.builder()
                        .id(favourite.getUser().getId())
                        .username(favourite.getUser().getUsername())
                        .build())
                .plant(PlantDto.builder()
                        .id(favourite.getPlant().getId())
                        .name(favourite.getPlant().getName())
                        .speciesName(favourite.getPlant().getSpeciesName())
                        .build())
                .build();

        return Response.build(Response.create("favourite"), favouriteDto, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deleteFavourite(Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Favourite> favourite = favouriteRepository.findById(id);

        if(favourite.isEmpty()){
            return Response.build("favourite not found", null, null, HttpStatus.BAD_REQUEST);
        }

        if(!favourite.get().getUser().getId().equals(user.get().getId())){
            return Response.build("not authorized", null, null, HttpStatus.UNAUTHORIZED);
        }

        favouriteRepository.deleteById(id);

        return Response.build(Response.delete("favourite"), null, null, HttpStatus.CREATED);
    }
}
