package com.seven.delivr.product.collection;

import com.seven.delivr.requests.AppRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class ProductCollectionUpsertRequest implements AppRequest {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9\\-\\'\\(\\) ]{2,100}$")
    public String name;
    public String image="";
}
