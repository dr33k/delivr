package com.seven.delivr.product;

import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.customer.cart.CartItem;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public record ProductMinifiedRecord(
        UUID productNo,
        String title,
        Double price,
        PublicEnum.Currency currency,
        String vendorName,
        List<String> images,
        Boolean isReady,
        Double ratingSum,
        Integer noOfRatings){
    public static ProductMinifiedRecord map(Product p){
        return new ProductMinifiedRecord(
                p.getProductNo(),
                p.getTitle(),
                p.getPrice(),
                p.getCurrency(),
                p.getVendor().getName(),
                Arrays.stream(p.getImages().split(";")).toList(),
                p.getIsReady(),
                p.getRatingSum(),
                p.getNoOfRatings());
    }
    public static ProductMinifiedRecord map(CartItem ci){
        Product p = ci.getCartItemId().getProduct();
        return new ProductMinifiedRecord(
                p.getProductNo(),
                p.getTitle(),
                p.getPrice(),
                p.getCurrency(),
                p.getVendor().getName(),
                Arrays.stream(p.getImages().split(";")).toList(),
                p.getIsReady(),
                p.getRatingSum(),
                p.getNoOfRatings());
    }

}
