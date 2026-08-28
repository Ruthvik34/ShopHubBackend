package com.microservice.ecommercewebappapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long productId;
    private String productName;
    private String productDescription;
    private double price;
    private int stock;
    private byte[] productImage;
    private LocalDateTime createdAt;


}
