package com.alterra.miniapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavouriteDto {
    private Long id;
    private UserDto user;
    private PlantDto plant;
}