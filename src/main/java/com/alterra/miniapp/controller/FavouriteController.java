package com.alterra.miniapp.controller;

import com.alterra.miniapp.domain.dto.CommentDto;
import com.alterra.miniapp.service.FavouriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
public class FavouriteController {

    @Autowired
    FavouriteService favouriteService;

    @PostMapping("/plants/{id}/favourites")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> add(@PathVariable Long id) {
        return favouriteService.addFavourite(id);
    }

    @DeleteMapping("/plants/favourites/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return favouriteService.deleteFavourite(id);
    }
}
