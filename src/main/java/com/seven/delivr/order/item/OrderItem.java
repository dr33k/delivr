package com.seven.delivr.order.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.base.BaseUUIDEntity;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.vendor.VendorOrder;
import com.seven.delivr.product.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "order_item")
@Data
@Builder
public class OrderItem extends BaseUUIDEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_order_id", referencedColumnName = "id")
    @JsonIgnore
    private VendorOrder vendorOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    @JsonIgnore
    private Product product;
    @Column( nullable = false)
    private Double price;
    @Column( nullable = false)
    private PublicEnum.Currency currency;
    @Column( nullable = false)
    private Integer units;
    @Column
    private String note;

    public OrderItem(){
        this.note = "";
    }

    public OrderItem(VendorOrder vendorOrder, Product product, Double price, PublicEnum.Currency currency, Integer units, String note) {
        this.vendorOrder = vendorOrder;
        this.product = product;
        this.price = price;
        this.currency = currency;
        this.units = units;
        this.note = note;
    }
    public boolean equals(BaseUUIDEntity other){
        return super.equals(other);
    }

    public int hashCode(){
        return super.hashCode();
    }
}
