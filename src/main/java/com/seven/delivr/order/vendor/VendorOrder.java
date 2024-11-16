package com.seven.delivr.order.vendor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.base.BaseUUIDEntity;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.customer.CustomerOrder;
import com.seven.delivr.order.item.OrderItem;
import com.seven.delivr.user.User;
import com.seven.delivr.vendor.Vendor;
import com.seven.delivr.vendor.account.VendorAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "vendor_order")
@Data
@Builder
@AllArgsConstructor
public class VendorOrder extends BaseUUIDEntity {
    @Column(name = "vendor_order_no", unique = true)
    private UUID vendorOrderNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_order_id", referencedColumnName = "id")
    @JsonIgnore
    private CustomerOrder customerOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vendor_id", referencedColumnName = "id")
    @JsonIgnore
    private Vendor vendor;
    @Column(name = "total_cost", nullable = false)
    private Double totalCost;
    @Enumerated(EnumType.STRING)
    private PublicEnum.Currency currency;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PublicEnum.OrderStatus status;
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;
    @OneToMany(fetch=FetchType.EAGER, mappedBy = "vendorOrder", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="vendor_account_id", referencedColumnName = "id")
    private VendorAccount vendorAccount;
    @Column(nullable = false)
    private Integer eta;

    public VendorOrder(){
        this.vendorOrderNo = UUID.randomUUID();
        this.totalCost = 0.0;
        this.orderItems = new ArrayList<>();
        this.currency = PublicEnum.Currency.NGN;
        this.eta = 0;
    }

    public User getUser(){return customerOrder.getUser();}
    public boolean equals(BaseUUIDEntity other){
        return super.equals(other);
    }

    public int hashCode(){
        return super.hashCode();
    }
}