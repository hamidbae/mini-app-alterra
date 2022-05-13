package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.dao.Comment;
import com.alterra.miniapp.domain.dao.Plant;
import com.alterra.miniapp.domain.dao.User;
import com.alterra.miniapp.domain.dto.CommentDto;
import com.alterra.miniapp.domain.dto.PlantDto;
import com.alterra.miniapp.domain.dto.UserDto;
import com.alterra.miniapp.repository.CommentRepository;
import com.alterra.miniapp.repository.PlantRepository;
import com.alterra.miniapp.repository.UserRepository;
import com.alterra.miniapp.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Object> addComment(Long plantId, CommentDto requestBody){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Plant> plant = plantRepository.findById(plantId);

        if(plant.isEmpty()){
            return Response.build("plant not found", null, null, HttpStatus.BAD_REQUEST);
        }

        Comment comment = Comment.builder()
//                .id(requestBody.getId())
                .plant(plant.get())
                .user(user.get())
                .text(requestBody.getText())
                .build();

        commentRepository.save(comment);

        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .user(UserDto.builder()
                        .id(comment.getUser().getId())
                        .name(comment.getUser().getName())
                        .username(comment.getUser().getUsername())
                        .build())
                .plant(PlantDto.builder()
                        .id(comment.getPlant().getId())
                        .name(comment.getPlant().getName())
                        .speciesName(comment.getPlant().getSpeciesName())
                        .build())
                .updatedAt(comment.getUpdatedAt())
                .createdAt(comment.getCreatedAt())
                .build();

        return Response.build(Response.create("comment"), commentDto, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updateComment(Long id, CommentDto requestBody){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Comment> comment = commentRepository.findById(id);

        if(comment.isEmpty()){
            return Response.build("comment not found", null, null, HttpStatus.BAD_REQUEST);
        }

        if(!comment.get().getUser().getId().equals(user.get().getId())){
            return Response.build("not authorized", null, null, HttpStatus.UNAUTHORIZED);
        }

        if(!requestBody.getText().isBlank()){
            comment.get().setText(requestBody.getText());
        }

        commentRepository.save(comment.get());

        CommentDto commentDto = CommentDto.builder()
                .id(comment.get().getId())
                .text(comment.get().getText())
                .user(UserDto.builder()
                        .id(comment.get().getUser().getId())
                        .name(comment.get().getUser().getName())
                        .username(comment.get().getUser().getUsername())
                        .build())
                .plant(PlantDto.builder()
                        .id(comment.get().getPlant().getId())
                        .name(comment.get().getPlant().getName())
                        .speciesName(comment.get().getPlant().getSpeciesName())
                        .build())
                .updatedAt(comment.get().getUpdatedAt())
                .createdAt(comment.get().getCreatedAt())
                .build();

        return Response.build(Response.update("comment"), commentDto, null, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deleteComment(Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Comment> comment = commentRepository.findById(id);

        if(comment.isEmpty()){
            return Response.build("comment not found", null, null, HttpStatus.BAD_REQUEST);
        }

        if(!comment.get().getUser().getId().equals(user.get().getId())){
            return Response.build("not authorized", null, null, HttpStatus.UNAUTHORIZED);
        }

        commentRepository.deleteById(id);

        return Response.build(Response.delete("comment"), null, null, HttpStatus.CREATED);
    }
}
