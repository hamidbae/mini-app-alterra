package com.alterra.miniapp.controller;

import com.alterra.miniapp.domain.dto.CommentDto;
import com.alterra.miniapp.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/plants/{id}/comments")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> add(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        return commentService.addComment(id, commentDto);
    }

    @PutMapping("/plants/comments/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(id, commentDto);
    }

    @DeleteMapping("/plants/comments/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }


}
