package com.seven.delivr.order.customer;

import com.seven.delivr.requests.AppRequest;
import com.seven.delivr.enums.PublicEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class CustomerOrderPatchRequest implements AppRequest {
    @NotNull
    public PublicEnum.OrderStatus status;
}
