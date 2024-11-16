package com.seven.delivr.order.vendor;

import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.item.OrderItemRecord;

import java.util.List;
import java.util.UUID;

public record VendorOrderRecord(
        UUID id,
        UUID vendorOrderNo,
        String vendorName,
        Double totalCost,
        PublicEnum.Currency currency,
        Boolean isPaid,
        List<OrderItemRecord> items
) {
    public static VendorOrderRecord map(VendorOrder vendorOrder){
        return new VendorOrderRecord(
                vendorOrder.getId(),
                vendorOrder.getVendorOrderNo(),
                vendorOrder.getVendor().getName(),
                vendorOrder.getTotalCost(),
                vendorOrder.getCurrency(),
                vendorOrder.getIsPaid(),
                vendorOrder.getOrderItems().stream().map(OrderItemRecord::map).toList()
        );
    }
}
