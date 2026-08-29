package com.microservice.ecommercewebappapi.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "name")
    private String productName;

    @Column(name = "title")
    private String productTitle;

    @Column(name = "description")
    private String productDescription;

    @Lob
    private byte[] productImage;

    @Column(name = "price")
    private Double productPrice;

    @Column(name = "stock")
    private Integer productStock;

    @Column(name = "rating")
    private Double productRating;

    @Column(name = "brand")
    private String productBrand;

    @Column(name = "category")
    private String productCategory;

    // You will need to change this later
    // to @ElementCollection or a separate Review entity
    @ElementCollection
    private List<String> productReviews;

    @Column(name = "active")
    private Boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}