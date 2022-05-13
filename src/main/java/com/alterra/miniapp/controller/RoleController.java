package com.alterra.miniapp.controller;

import com.alterra.miniapp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<Object> createRoles() {
        return roleService.createRoles();
    }
}
