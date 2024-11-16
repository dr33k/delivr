package com.seven.delivr.order.vendor;

import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.user.User;
import com.seven.delivr.vendor.Vendor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendorOrderRepository extends JpaRepository<VendorOrder, UUID> {

    List<VendorOrder> findAllByVendor(Vendor vendor, Pageable pageable);
    List<VendorOrder> findAllByStatusAndVendorAndDateCreated(PublicEnum.OrderStatus status, Vendor vendor, ZonedDateTime dateCreated, Pageable pageable);

    List<VendorOrder> findAllByStatusAndUserAndDateCreated(PublicEnum.OrderStatus status, User customer, ZonedDateTime dateCreated, Pageable pageable);

    List<VendorOrder> findAllByStatusAndUser(PublicEnum.OrderStatus status, User customer,Pageable pageable);

    Optional<VendorOrder> findByIdAndVendor(UUID id, Vendor vendor);
    @Modifying
    void deleteByIdAndVendor(UUID id, Vendor vendor);

    @Query(value = "SELECT COUNT(vo.id) AS noOfOrders FROM VendorOrder vo WHERE vo.status = 'PAID'")
    Integer getNoOfPendingTransit();

    @Query(value = "SELECT COUNT(vo.id) FROM VendorOrder vo WHERE vo.status = 'DISPATCHED'")
    Integer getNoOfInTransit();
    @Query(value = "SELECT COUNT(vo.id) FROM VendorOrder vo WHERE vo.status = 'DELIVERED'")
    Integer getNoOfDelivered();

    @Query(value = "SELECT SUM(vo.totalCost) FROM VendorOrder vo WHERE vo.status <> 'CREATED'")
    Double getCummulativeAmount();

}