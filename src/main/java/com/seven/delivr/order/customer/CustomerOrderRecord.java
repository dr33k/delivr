package com.seven.delivr.order.customer;

import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.vendor.VendorOrderRecord;
import com.seven.delivr.user.location.Location;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record CustomerOrderRecord(
        UUID id,
        UUID userId,
        Double totalCost,
        PublicEnum.Currency currency,
        Location location,
        Boolean isPaid,
        Double serviceFee,
        Double deliveryFee,
        Integer noOfVendorOrders,
        Integer noOfReadyVendorOrders,
        ZonedDateTime dateCreated,
        List<VendorOrderRecord> vendorOrders
) {
    public static CustomerOrderRecord map(CustomerOrder co){
        return new CustomerOrderRecord(
                co.getId(),
                co.getUser().getId(),
                co.getTotalCost(),
                co.getCurrency(),
                co.getLocation(),
                co.getIsPaid(),
                co.getServiceFee(),
                co.getDeliveryFee(),
                co.getNoOfVendorOrders(),
                co.getNoOfReadyVendorOrders(),
                co.getDateCreated(),
                co.getVendorOrders().stream().map(VendorOrderRecord::map).toList()
        );
    }
}
