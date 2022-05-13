package com.alterra.miniapp.domain.dto;

import com.alterra.miniapp.domain.common.BaseTimestamp;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto extends BaseTimestamp {
    private Long id;
    private UserDto user;
    private PlantDto plant;
    private String text;
}