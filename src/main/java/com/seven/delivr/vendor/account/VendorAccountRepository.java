package com.seven.delivr.vendor.account;

import com.seven.delivr.vendor.Vendor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorAccountRepository extends JpaRepository<VendorAccount, UUID> {
    List<VendorAccount> findAllByVendor(Vendor vendor, Pageable pageable);
    List<VendorAccount> findAllByVendor(Vendor vendor);
    Optional<VendorAccount> findByIdAndVendor(UUID id, Vendor vendor);
    void deleteByIdAndVendor(UUID id, Vendor vendor);
}
