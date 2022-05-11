package com.alterra.miniapp.domain.dto;

import com.alterra.miniapp.domain.common.BaseCreatedAt;
import com.alterra.miniapp.domain.common.BaseIsDeleted;
import com.alterra.miniapp.domain.common.BaseUpdatedAt;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends BaseIsDeleted {
    private Long id;
    private String username;
    private String password;
    private String name;
    private Set<String> roles;
}
