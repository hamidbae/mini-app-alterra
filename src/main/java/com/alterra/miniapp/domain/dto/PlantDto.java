package com.alterra.miniapp.domain.dto;

import com.alterra.miniapp.domain.common.BaseTimestamp;
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
public class PlantDto extends BaseTimestamp {
    private Long id;
    private String name;
    private String speciesName;
    private String content;
}
