package com.seven.delivr.product.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.base.BaseLongEntity;
import com.seven.delivr.base.BaseUUIDEntity;
import com.seven.delivr.product.Product;
import com.seven.delivr.util.Utilities;
import com.seven.delivr.vendor.Vendor;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_collection")
@Data
public class ProductCollection extends BaseUUIDEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String image="";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    @JsonIgnore
    private Vendor vendor;
    @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();
    public boolean equals(BaseLongEntity other){
        return super.equals(other);
    }
    public int hashCode(){
        return super.hashCode();
    }

    public ProductCollection(){}

    public ProductCollection(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public static ProductCollection creationMap(ProductCollectionUpsertRequest r){
        r.name = Utilities.escape(r.name);
        return new ProductCollection(r.name, r.image);
    }    public static void updateMap(ProductCollectionUpsertRequest r, ProductCollection pc){
        r.name = Utilities.escape(r.name);
        pc.name = r.name;
        if(!Utilities.isEmpty(r.image)) pc.image = r.image;
    }
}
