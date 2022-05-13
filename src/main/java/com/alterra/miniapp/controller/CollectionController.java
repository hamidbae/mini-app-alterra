package com.alterra.miniapp.controller;

import com.alterra.miniapp.service.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    @PostMapping("/plants/{id}/collections")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> add(@PathVariable Long id) {
        return collectionService.addCollection(id);
    }

    @DeleteMapping("/plants/collections/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return collectionService.deleteCollection(id);
    }
}
