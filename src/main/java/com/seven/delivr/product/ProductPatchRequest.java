package com.seven.delivr.product;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
@Validated
public class ProductPatchRequest extends ProductCreateRequest{
    @NotNull
    public UUID productNo;
    public Integer deliveryTimeMins;
}
