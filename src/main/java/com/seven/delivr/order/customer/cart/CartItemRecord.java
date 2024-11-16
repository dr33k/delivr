package com.seven.delivr.order.customer.cart;

import com.seven.delivr.product.ProductMinifiedRecord;

public record CartItemRecord (
        ProductMinifiedRecord productItem,
        Integer units,
        String note
){
    public static CartItemRecord map(CartItem ci){
        return new CartItemRecord(
                ProductMinifiedRecord.map(ci.getCartItemId().getProduct()),
                ci.getUnits(),
                ci.getNote()
        );
    }
}
