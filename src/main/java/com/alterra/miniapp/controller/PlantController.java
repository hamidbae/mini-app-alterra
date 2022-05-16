package com.alterra.miniapp.controller;

import com.alterra.miniapp.domain.dto.PlantDto;
import com.alterra.miniapp.service.PlantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
public class PlantController {

    @Autowired
    PlantService plantService;

    @GetMapping("/plants")
    public ResponseEntity<Object> getAll() {
        return plantService.getAllPlants();
    }

    @GetMapping("/plants/{id}")
    public ResponseEntity<Object> getOne(@PathVariable Long id) {
        return plantService.getOnePlant(id);
    }

    @PostMapping("/plants")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> add(@RequestBody PlantDto plantDto) {
        return plantService.addPlant(plantDto);
    }

    @DeleteMapping("/plants/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return plantService.deletePlant(id);
    }

    @PutMapping("/plants/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody PlantDto plantDto) {
        return plantService.updatePlant(id, plantDto);
    }

    @GetMapping("/plants-detail")
    public ResponseEntity<Object> getAllDetail() {
        return plantService.getPlantsDetail();
    }
}
