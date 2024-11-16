package com.seven.delivr.product;

import com.seven.delivr.product.collection.ProductCollection;
import com.seven.delivr.vendor.Vendor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByIsPublished(Boolean isPublished, Pageable pageable);

    List<Product> findAllByVendor(Vendor vendor, Pageable pageable);
    @Query("SELECT p.id, p.ratingSum, p.noOfRatings FROM Product p")
    List<Product> findAllWithRatingByVendor(Vendor vendor);
    @Modifying
    @Query(value="UPDATE Product p SET p.isPublished = ?2 WHERE p.productNo = ?1")
    void setIsPublishedByProductNo(UUID productNo, Boolean isPublished);
    @Modifying
    @Query(value="UPDATE Product p SET p.isReady = ?2 WHERE p.productNo = ?1")
    void setIsReadyByProductNo(UUID productNo, Boolean isReady);

    Boolean existsByProductNo(UUID productNo);
    Optional<Product> findByProductNo(UUID productNo);
    List<Product> findAllByProductNoIn(List<UUID> productNos);
    void deleteByProductNo(UUID productNo);

    List<Product> findAllByCollection(ProductCollection collection, Pageable pageable);

}
