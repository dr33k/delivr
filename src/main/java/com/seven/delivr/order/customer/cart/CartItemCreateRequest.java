package com.seven.delivr.order.customer.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public class CartItemCreateRequest {
    @NotNull
    public UUID productNo;
    @NotNull
    @Min(1)
    public Integer units;
    @Pattern(regexp = "^[A-Za-z0-9\\n\\-\\'\\(\\)\\,\\.\\: ]{0,200}$", message = "Special characters not allowed")
    public String note;
}
