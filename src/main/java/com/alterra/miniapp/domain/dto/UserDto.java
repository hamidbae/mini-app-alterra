package com.alterra.miniapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto{
    private Long id;
    private String username;
    private String password;
    private String name;
    private Set<String> roles;
}
