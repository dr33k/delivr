package com.seven.delivr.order.vendor;

import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.order.item.OrderItem;
import com.seven.delivr.user.location.Location;
import com.seven.delivr.vendor.account.VendorAccount;

import java.util.List;
import java.util.UUID;

public record VendorOrderDetailedRecord(UUID id,
                                        UUID vendorOrderNo,
                                        Double totalCost,
                                        PublicEnum.Currency currency,
                                        Boolean isPaid,
                                        Location location,
                                        PublicEnum.OrderStatus status,
                                        List<OrderItem> orderItems,
                                        VendorAccount vendorAccount) {
    public static VendorOrderDetailedRecord map(VendorOrder vo) {
        return new VendorOrderDetailedRecord(
                vo.getId(),
                vo.getVendorOrderNo(),
                vo.getTotalCost(),
                vo.getCurrency(),
                vo.getIsPaid(),
                vo.getCustomerOrder().getLocation(),
                vo.getStatus(),
                vo.getOrderItems(),
                vo.getVendorAccount()
        );

    }
}
