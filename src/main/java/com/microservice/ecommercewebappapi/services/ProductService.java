package com.microservice.ecommercewebappapi.services;

import com.microservice.ecommercewebappapi.dto.ProductResponseDTO;
import com.microservice.ecommercewebappapi.models.Products;
import com.microservice.ecommercewebappapi.repository.ProductRepository;
import com.microservice.ecommercewebappapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public List<Products> getAllProducts(){
        return productRepository.findAll();
    }

    public Products findProductById(Long id){
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("Product Not Found"));
    }

    public Products registerProduct(Products product) {

       return productRepository.save(product);

    }



    public String deleteProduct(Long productId){
        productRepository.deleteById(productId);
        return "Product Deleted Successfully";

    }


    public String updateProduct(
            Products product,
            Long id,
            MultipartFile image) throws IOException {

        Products prod = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product Not Found"));

        if (product.getProductName() != null) {
            prod.setProductName(product.getProductName());
        }

        if (product.getProductTitle() != null) {
            prod.setProductTitle(product.getProductTitle());
        }

        if (product.getProductDescription() != null) {
            prod.setProductDescription(
                    product.getProductDescription());
        }

        if (product.getProductBrand() != null) {
            prod.setProductBrand(product.getProductBrand());
        }

        if (product.getProductCategory() != null) {
            prod.setProductCategory(
                    product.getProductCategory());
        }

        if (product.getProductPrice() != null) {
            prod.setProductPrice(product.getProductPrice());
        }

        if (product.getProductStock() != null) {
            prod.setProductStock(product.getProductStock());
        }

        if (product.getProductRating() != null) {
            prod.setProductRating(product.getProductRating());
        }

        if (product.getActive() != null) {
            prod.setActive(product.getActive());
        }

        // Update image only when a new image is provided
        if (image != null && !image.isEmpty()) {
            prod.setProductImage(image.getBytes());
        }

        productRepository.save(prod);

        return "Product Updated Successfully";
    }


}
