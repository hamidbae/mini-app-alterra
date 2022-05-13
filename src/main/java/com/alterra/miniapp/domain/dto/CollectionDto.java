package com.alterra.miniapp.domain.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto {
    private Long id;
    private UserDto user;
    private PlantDto plant;
}