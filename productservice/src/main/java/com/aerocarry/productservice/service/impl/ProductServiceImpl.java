package com.aerocarry.productservice.service.impl;

import com.aerocarry.productservice.dto.ProductRequest;
import com.aerocarry.productservice.dto.ProductResponse;
import com.aerocarry.productservice.entity.Feature;
import com.aerocarry.productservice.entity.Product;
import com.aerocarry.productservice.exception.ResourceNotFoundException;
import com.aerocarry.productservice.repository.ProductRepository;
import com.aerocarry.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Helper method to map Entity to DTO
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .sku(product.getSku())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .type(product.getType().getName())
                .category(product.getCategory().getName())
                .size(product.getSize().getName())
                .color(product.getColor().getName())
                .features(product.getFeatures().stream()
                        .map(Feature::getName)
                        .collect(Collectors.toList()))
                .build();
    }

    // ... inside ProductServiceImpl.java ...

    // --- ADMIN METHODS ---

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        // Here you would fetch the Type, Category, Size, etc. from their Repositories using the IDs
        // For example: Type type = typeRepository.findById(request.getTypeId()).orElseThrow(...);

        Product product = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                // .type(type) ... set the fetched entities here
                .build();

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // --- PUBLIC METHODS ---

    @Override
    public Page<ProductResponse> searchProducts(Long typeId, Long categoryId, Long sizeId,
                                                Long colorId, BigDecimal minPrice,
                                                BigDecimal maxPrice, int page, int size) {

        // Setup pagination and sorting (e.g., sort by price descending)
        Pageable pageable = PageRequest.of(page, size, Sort.by("price").descending());

        Page<Product> productPage = productRepository.searchProducts(
                typeId, categoryId, sizeId, colorId, minPrice, maxPrice, pageable);

        return productPage.map(this::mapToResponse);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        return null;
    }
}