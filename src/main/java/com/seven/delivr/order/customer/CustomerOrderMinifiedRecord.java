package com.seven.delivr.order.customer;

import com.seven.delivr.order.vendor.VendorOrderRecord;

import java.util.List;
import java.util.UUID;

public record CustomerOrderMinifiedRecord(
        UUID id,
        UUID userId,
        Boolean isPaid,
        Integer noOfVendorOrders,
        Integer noOfReadyVendorOrders,
        List<VendorOrderRecord> vendorOrders
) {
    public static CustomerOrderMinifiedRecord map(CustomerOrder co){
        return new CustomerOrderMinifiedRecord(
                co.getId(),
                co.getUser().getId(),
                co.getIsPaid(),
                co.getNoOfVendorOrders(),
                co.getNoOfReadyVendorOrders(),
                co.getVendorOrders().stream().map(VendorOrderRecord::map).toList()
        );
    }
}
