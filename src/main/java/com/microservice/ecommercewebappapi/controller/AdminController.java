package com.microservice.ecommercewebappapi.controller;

import com.microservice.ecommercewebappapi.Enums.Roles;
import com.microservice.ecommercewebappapi.models.Users;
import com.microservice.ecommercewebappapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PatchMapping("/users/{email}/roles/secure")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserRole(
            @PathVariable String email,
            @RequestParam Roles role) {

        return ResponseEntity.ok(
                userService.updateUserRole(email, role)
        );
    }

    @DeleteMapping("/users/{email}/roles/secure")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeUserRole(
            @PathVariable String  email,
            @RequestParam Roles role) {

        return ResponseEntity.ok(
                userService.removeUserRole(email, role)
        );
    }

    @GetMapping("/users/secure")
    public ResponseEntity<List<Users>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }
}
