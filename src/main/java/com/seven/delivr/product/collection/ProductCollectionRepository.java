package com.seven.delivr.product.collection;

import com.seven.delivr.vendor.Vendor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCollectionRepository extends JpaRepository<ProductCollection, UUID> {
    List<ProductCollection> findAllByVendor(Vendor vendor, Pageable pageable);
    Optional<ProductCollection> findByIdAndVendor(UUID id, Vendor vendor);
    Boolean existsByNameAndVendor(String name, Vendor vendor);
    void deleteByIdAndVendor(UUID id, Vendor vendor);

}
