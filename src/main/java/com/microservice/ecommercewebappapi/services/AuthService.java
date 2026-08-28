package com.microservice.ecommercewebappapi.services;

import com.microservice.ecommercewebappapi.Enums.Roles;
import com.microservice.ecommercewebappapi.dto.JwtResponse;
import com.microservice.ecommercewebappapi.dto.LoginRequest;
import com.microservice.ecommercewebappapi.dto.RegisterUserDto;
import com.microservice.ecommercewebappapi.dto.UpdateUserProfileRequest;
import com.microservice.ecommercewebappapi.models.RefreshToken;
import com.microservice.ecommercewebappapi.models.Users;
import com.microservice.ecommercewebappapi.repository.RefreshTokenRepository;
import com.microservice.ecommercewebappapi.repository.UserRepository;
import com.microservice.ecommercewebappapi.security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuthenticationManager manager;

    private final UserDetailsService userDetailsService;

    private final JwtUtils helper;

    private final RefreshTokenRepository refreshTokenRepository;


    public RegisterUserDto registerUser(Users user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(
                new ArrayList<>(List.of(Roles.ROLE_USER))
        );

        Users savedUser = userRepository.save(user);

        return RegisterUserDto
                .builder()
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    public JwtResponse login(LoginRequest request) {

        // 1. Authenticate user
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        // 2. Load user
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getUserName()
                );

        // 3. Generate access token
        String accessToken =
                helper.generateToken(userDetails);

        // 4. Generate refresh token
        String refreshToken =
                helper.generateRefreshToken(
                        userDetails.getUsername()
                );

        // 5. Get Users entity
        Users user =
                userRepository.findUsersByEmail(
                        userDetails.getUsername()
                ).orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        // 6. Store refresh token
        RefreshToken refreshTokenEntity =
                new RefreshToken();

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiryDate(
                helper.getExpirationDateFromToken(
                        refreshToken
                )
        );

        refreshTokenRepository.save(refreshTokenEntity);

        // 7. Return response
        return JwtResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .userName(userDetails.getUsername())
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {

        refreshTokenRepository.deleteByToken(refreshToken);
    }

    public Map<String, String> refresh(String refreshToken) {

        // Check if token exists in DB
        RefreshToken storedToken =
                refreshTokenRepository
                        .findByToken(refreshToken)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Refresh token not found"
                                )
                        );

        // Validate JWT
        if (!helper.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Check DB expiration
        if (storedToken.getExpiryDate().before(new Date())) {

            refreshTokenRepository.delete(storedToken);

            throw new RuntimeException("Refresh token expired");
        }

        String username =
                helper.getUsernameFromToken(refreshToken);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        String newAccessToken =
                helper.generateToken(userDetails);

        return Map.of(
                "token", newAccessToken
        );
    }



}
