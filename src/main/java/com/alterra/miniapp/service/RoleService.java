package com.alterra.miniapp.service;

import com.alterra.miniapp.domain.dao.ERole;
import com.alterra.miniapp.domain.dao.Role;
import com.alterra.miniapp.repository.RoleRepository;
import com.alterra.miniapp.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Temporal;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public ResponseEntity<Object> createRoles(){
        Optional<Role> adminRole = roleRepository.findByName(ERole.ADMIN);
        Optional<Role> userRole = roleRepository.findByName(ERole.USER);
        if(!adminRole.isEmpty()){
            return Response.build(Response.exist("roles", "name", "admin"), null, null, HttpStatus.BAD_REQUEST);
        }
        if(!userRole.isEmpty()){
            return Response.build(Response.exist("roles", "name", "user"), null, null, HttpStatus.BAD_REQUEST);
        }

        roleRepository.save(Role.builder().name(ERole.USER).build());
        roleRepository.save(Role.builder().name(ERole.ADMIN).build());

        List<Role> roles = roleRepository.findAll();
        return Response.build(Response.get("Role"), roles, null, HttpStatus.OK);
    }
}
