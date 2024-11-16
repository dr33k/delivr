package com.seven.delivr.order.customer;

import com.seven.delivr.requests.AppRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public class CustomerOrderCreateRequest implements AppRequest {
    @NotNull(message = "Customer location is missing")
    public UUID customerLocationId;
}
