package com.seven.delivr.order.payment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public class PaymentRequest {
    @NotNull
    public UUID customerOrderId;
}
