package com.seven.delivr.order.item;

import com.seven.delivr.enums.PublicEnum;

import java.util.UUID;

public record OrderItemRecord(
        UUID productNo,
        String productTitle,
        Double total,
        PublicEnum.Currency currency,
        Integer units

) {
    public static OrderItemRecord map(OrderItem oi){
        int units = oi.getUnits();
        return new OrderItemRecord(
                oi.getProduct().getProductNo(),
                oi.getProduct().getTitle(),
                oi.getPrice() * units,
                oi.getCurrency(),
                units
        );
    }
}
