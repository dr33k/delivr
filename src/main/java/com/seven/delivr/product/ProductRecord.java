package com.seven.delivr.product;

import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.product.collection.ProductCollection;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public record ProductRecord(
        UUID productNo,
        String title,
        String description,
        Double price,
        PublicEnum.Currency currency,
        PublicEnum.ProductType type,
        List<String> images,
        Boolean isReady,
        Double ratingSum,
        Integer noOfRatings,
        Integer preparationTimeMins,
        Integer deliveryTimeMins,
        String vendorName,
        String collectionName
) {

    public static ProductRecord map(Product p){
        ProductCollection pc = p.getCollection();
        return new ProductRecord(
                p.getProductNo(),
                p.getTitle(),
                p.getDescription(),
                p.getPrice(),
                p.getCurrency(),
                p.getType(),
                Arrays.stream(p.getImages().split(";")).toList(),
                p.getIsReady(),
                p.getRatingSum(),
                p.getNoOfRatings(),
                p.getPreparationTimeMins(),
                p.getDeliveryTimeMins(),
                p.getVendor().getName(),
                pc == null? "" : pc.getName()
        );
    }
}
