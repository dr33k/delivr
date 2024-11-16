package com.seven.delivr.product;

import com.seven.delivr.base.BaseLongEntity;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.product.collection.ProductCollection;
import com.seven.delivr.util.Utilities;
import com.seven.delivr.vendor.Vendor;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "product")
@Data
public class Product extends BaseLongEntity {
    @Column(name = "product_no", unique = true, nullable = false)
    private UUID productNo;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PublicEnum.Currency currency = PublicEnum.Currency.NGN;
    @Column(nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private PublicEnum.ProductType type = PublicEnum.ProductType.OTHER;

    @Column
    private String images;
    @Column(name = "is_ready")
    private Boolean isReady = true;
    @Column(name = "is_published")
    private Boolean isPublished = true;
    @Column(name = "rating_sum")
    private Double ratingSum = 3.0;
    @Column(name = "no_of_ratings")
    private Integer noOfRatings = 1;
    @Column(name = "preparation_time_mins")
    private Integer preparationTimeMins = 0;
    @Column(name = "delivery_time_mins")
    private Integer deliveryTimeMins = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_collection_id", referencedColumnName = "id")
    private ProductCollection collection;

    public Product(){
        this.productNo = UUID.randomUUID();
    }

    //Constructor is used by ProductRepository::findAllWithRatingByVendor
    public Product(Long id, Double ratingSum, Integer noOfRatings) {
        this.id = id;
        this.ratingSum = ratingSum;
        this.noOfRatings = noOfRatings;
    }

    //Constructor is used by ProductService::filter
    public Product(UUID productNo, String title, Double price, PublicEnum.Currency currency, String images,
                   Boolean isReady, Double ratingSum, Integer noOfRatings, String vendorName, String collectionName){
        this.productNo = productNo;
        this.title = title;
        this.price = price;
        this.currency = currency;
        this.images = images;
        this.isReady = isReady;
        this.ratingSum = ratingSum;
        this.noOfRatings = noOfRatings;
        Vendor v = new Vendor();
        v.setName(vendorName);
        this.vendor = v;
        ProductCollection pc = new ProductCollection();
        pc.setName(collectionName);
        this.collection = pc;
    }
    //Constructor is used by Product::creationMap
    public Product(String title, String description, Double price, PublicEnum.Currency currency, String images,
                   Boolean isReady, Integer preparationTimeMins, PublicEnum.ProductType type) {
        this.productNo = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.images = images;
        this.isReady = isReady;
        this.preparationTimeMins = preparationTimeMins;
        this.type = type;
    }

    public static Product creationMap(ProductCreateRequest r) {
        return new Product(
                Utilities.escape(r.title),
                Utilities.escape(r.description),
                r.price,
                r.currency,
                r.images.stream().reduce((a, b)-> String.format("%s;%s",a,b)).orElse(" "),
                r.isReady,
                r.preparationTimeMins,
                r.type
        );
    }

    public static void updateMap(ProductPatchRequest r, Product p) {
        if (!Utilities.isEmpty(r.title)) p.setTitle(Utilities.escape(Utilities.clean(r.title, "[^A-Za-z0-9\\-\\'\\(\\) ]")));
        if (!Utilities.isEmpty(r.description))
            p.setDescription(Utilities.escape(Utilities.clean(r.description, "[^A-Za-z0-9\\n\\-\\'\\(\\) ]")));
        if (r.images != null && !r.images.isEmpty()){
            String reduced = r.images.stream().reduce((a, b)-> String.format("%s;%s",a,b)).orElse(" ");
            p.setImages(reduced);
        }
        if (r.price != null) p.setPrice(r.price);
        if (r.type != null) p.setType(r.type);
        if (r.currency != null) p.setCurrency(r.currency);
        if (r.preparationTimeMins != null) p.setPreparationTimeMins(r.preparationTimeMins);
        if (r.deliveryTimeMins != null) p.setDeliveryTimeMins(r.deliveryTimeMins);
    }

    public boolean equals(BaseLongEntity other){
        return super.equals(other);
    }

    public int hashCode(){
        return super.hashCode();
    }
}
