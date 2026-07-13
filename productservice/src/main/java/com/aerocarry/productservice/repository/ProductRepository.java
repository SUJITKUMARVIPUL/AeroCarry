package com.aerocarry.productservice.repository;

import com.aerocarry.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);

    // Dynamic JPQL Search with Pagination
    @Query("SELECT p FROM Product p WHERE " +
            "(:typeId IS NULL OR p.type.id = :typeId) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
            "(:sizeId IS NULL OR p.size.id = :sizeId) AND " +
            "(:colorId IS NULL OR p.color.id = :colorId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> searchProducts(
            @Param("typeId") Long typeId,
            @Param("categoryId") Long categoryId,
            @Param("sizeId") Long sizeId,
            @Param("colorId") Long colorId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
}