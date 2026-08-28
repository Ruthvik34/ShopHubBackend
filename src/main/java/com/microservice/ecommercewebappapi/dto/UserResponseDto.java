package com.microservice.ecommercewebappapi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String Phone;

    private LocalDateTime createdAt;
}
