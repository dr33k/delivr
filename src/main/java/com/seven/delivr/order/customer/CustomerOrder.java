package com.seven.delivr.order.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.vendor.VendorOrder;
import com.seven.delivr.rider.Rider;
import com.seven.delivr.user.User;
import com.seven.delivr.user.location.Location;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer_order")
@Data
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    @JsonIgnore
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customerOrder", cascade = CascadeType.ALL)
    private List<VendorOrder> vendorOrders = new ArrayList<>();
    @Column(name = "total_cost", nullable = false)
    private Double totalCost = 0.0;
    @Column(nullable = false)
    private PublicEnum.Currency currency;
    @JoinColumn(referencedColumnName = "id", name = "location_id")
    @ManyToOne
    private Location location;
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;
    @Column(name="service_fee", nullable = false)
    private Double serviceFee;
    @Column(name="delivery_fee", nullable = false)
    private Double deliveryFee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rider_id", referencedColumnName = "id")
    private Rider rider;
    @Column(nullable = false, name="no_of_vendor_orders")
    private Integer noOfVendorOrders;
    @Column(nullable = false, name = "no_of_ready_vendor_orders")
    private Integer noOfReadyVendorOrders;
    @CreationTimestamp
    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    public CustomerOrder() {
        noOfReadyVendorOrders = 0;
    }

    public CustomerOrder(User user, Location location) {
        this.user = user;
        this.location = location;
        noOfReadyVendorOrders = 0;
    }
}
