package com.seven.delivr.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    @Modifying
    @Query(value = "UPDATE app_user SET vendor_id = :vendorId WHERE id = :id", nativeQuery = true)
    void setVendorById(UUID id, Long vendorId);

}