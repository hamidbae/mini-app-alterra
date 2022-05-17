package com.alterra.miniapp.domain.dto;


import com.alterra.miniapp.domain.common.BaseCreatedAt;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionDto extends BaseCreatedAt {
    private Long id;
    private UserDto user;
    private PlantDto plant;
}