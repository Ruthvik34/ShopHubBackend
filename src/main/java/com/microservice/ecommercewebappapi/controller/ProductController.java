package com.microservice.ecommercewebappapi.controller;

import com.microservice.ecommercewebappapi.models.Products;
import com.microservice.ecommercewebappapi.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable Long id) {

        return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);

    }

    @DeleteMapping("/{id}/secure")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<String> deleteProduct(@PathVariable Long id){

        return new ResponseEntity<>(productService.deleteProduct(id),HttpStatus.OK);

    }

    @PatchMapping(
            value = "/{id}/secure",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") Products product,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {

        return ResponseEntity.ok(
                productService.updateProduct(product, id, image)
        );
    }

    @GetMapping("/{id}/image/secure")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> getProductImage(
            @PathVariable Long id) {

        Products product = productService.findProductById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(product.getProductImage());
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Products> registerProduct(
            @RequestPart("product") Products products,
            @RequestPart("image") MultipartFile image) throws IOException {

        products.setProductImage(image.getBytes());

        return new ResponseEntity<>(
                productService.registerProduct(products),
                HttpStatus.OK
        );
    }
}
