package com.microservice.ecommercewebappapi.controller;

import com.microservice.ecommercewebappapi.dto.JwtResponse;
import com.microservice.ecommercewebappapi.dto.LoginRequest;
import com.microservice.ecommercewebappapi.dto.RefreshRequest;
import com.microservice.ecommercewebappapi.dto.RegisterUserDto;
import com.microservice.ecommercewebappapi.models.Users;
import com.microservice.ecommercewebappapi.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserDto> registerUser(
            @RequestBody Users user) {

        return ResponseEntity.ok(
                authService.registerUser(user)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody RefreshRequest request) {

        return ResponseEntity.ok(
                authService.refresh(request.getRefreshToken())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody RefreshRequest request) {

        authService.logout(request.getRefreshToken());

        return ResponseEntity.ok("Logout successful");
    }
}