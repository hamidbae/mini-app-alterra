package com.alterra.miniapp.domain.dto;

import com.alterra.miniapp.domain.common.BaseTimestamp;
import com.alterra.miniapp.domain.common.BaseUpdatedAt;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlantDto extends BaseUpdatedAt {
    private Long id;
    private String name;
    private String speciesName;
    private String content;
}
