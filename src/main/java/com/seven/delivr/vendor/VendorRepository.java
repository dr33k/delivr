package com.seven.delivr.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Boolean existsByIncorporationNo(Integer incorporationNo);
    Boolean existsByMgmtEmail(String mgmtEmail);
    @Query(value="SELECT v.id, v.name, v.rating FROM Vendor v ORDER BY v.rating DESC")
    List<Vendor> getVendorWithRatings();
    @Query(value="SELECT v.id, v.name, v.rating FROM Vendor v ORDER BY v.rating DESC LIMIT :limit OFFSET :offset")
    List<Vendor> getVendorWithRatings(Integer limit, Integer offset);
}
