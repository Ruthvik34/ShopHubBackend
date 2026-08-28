package com.microservice.ecommercewebappapi.repository;

import com.microservice.ecommercewebappapi.models.RefreshToken;
import com.microservice.ecommercewebappapi.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUser(Users user);
}