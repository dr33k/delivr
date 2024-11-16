package com.seven.delivr.order.vendor;

import com.seven.delivr.enums.PublicEnum;

import java.util.UUID;

public record VendorOrderMinifiedRecord(
        UUID id,
        UUID vendorOrderNo,
        Double totalCost,
        PublicEnum.Currency currency,
        Boolean isPaid
) {
    public static VendorOrderMinifiedRecord map(VendorOrder vendorOrder){
        return new VendorOrderMinifiedRecord(
                vendorOrder.getId(),
                vendorOrder.getVendorOrderNo(),
                vendorOrder.getTotalCost(),
                vendorOrder.getCurrency(),
                vendorOrder.getIsPaid()
        );
    }
}
