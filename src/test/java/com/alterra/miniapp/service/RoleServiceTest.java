package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.common.ApiResponse;
import com.alterra.miniapp.domain.dao.ERole;
import com.alterra.miniapp.domain.dao.Role;
import com.alterra.miniapp.repository.RoleRepository;
import com.alterra.miniapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RoleService.class)
@Slf4j
public class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Test
    void createRolesSuccess_Test(){
        Role userRole = Role.builder().id(1L).name(ERole.USER).build();
        Role adminRole = Role.builder().id(2L).name(ERole.ADMIN).build();
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        roles.add(adminRole);

        Mockito.when(roleRepository.save(userRole)).thenReturn(userRole);
        Mockito.when(roleRepository.save(adminRole)).thenReturn(adminRole);
        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        ResponseEntity<Object> response = roleService.createRoles();

        ApiResponse apiResponse = (ApiResponse) response.getBody();
        List<Role> data = (List<Role>) Objects.requireNonNull(apiResponse).getData();
        Assertions.assertEquals(2, data.size());
        Assertions.assertEquals(roles, data);
    }
}
