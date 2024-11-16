package com.seven.delivr.product.collection;

import com.seven.delivr.product.ProductMinifiedRecord;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record ProductCollectionRecord(
        UUID id,
        String name,
        String image,
        List<ProductMinifiedRecord> products,
        ZonedDateTime dateCreated
) {
    public static ProductCollectionRecord map(ProductCollection pc){
        return new ProductCollectionRecord(
                pc.getId(),
                pc.getName(),
                pc.getImage(),
                pc.getProducts().stream().map(ProductMinifiedRecord::map).toList(),
                pc.getDateCreated()
        );
    }
}
