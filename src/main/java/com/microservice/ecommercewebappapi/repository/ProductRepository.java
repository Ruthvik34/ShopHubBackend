package com.microservice.ecommercewebappapi.repository;

import com.microservice.ecommercewebappapi.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {
}
