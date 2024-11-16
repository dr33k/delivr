package com.seven.delivr.order.customer;

import com.seven.delivr.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, UUID>{
    List<CustomerOrder> findAllByUserAndIsPaid(User user, Boolean isPaid, Pageable pageable);
    Optional<CustomerOrder> findByIdAndUser(UUID id, User user);
    void deleteByIdAndUser(UUID id, User user);

    @Query("SELECT co FROM CustomerOrder co WHERE co.noOfReadyVendorOrders = co.noOfVendorOrders AND co.rider IS null"+
            " AND co.dateCreated > :fromTime ORDER BY co.dateCreated ASC")
    List<CustomerOrder> getStarvedOrders(ZonedDateTime fromTime);

}
