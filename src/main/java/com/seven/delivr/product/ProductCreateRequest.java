package com.seven.delivr.product;

import com.seven.delivr.enums.PublicEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Validated
public class ProductCreateRequest extends ProductRequest{
    @NotBlank(message = "Title is empty")
    @Pattern(regexp ="^[A-Za-z0-9\\-\\'\\(\\) ]{2,100}$", message = "Title must contain only numbers and alphabets between 2 to 100 characters")
    public String title;
    @Pattern(regexp = "^[A-Za-z0-9\\n\\-\\'\\(\\)\\.\\,\\? ]{2,200}$", message = "Description must contain only numbers and alphabets between 2 to 200 characters")
    public String description;
    @NotNull(message = "Price is empty")
    public Double price;
    public PublicEnum.Currency currency = PublicEnum.Currency.NGN;
    public List<String> images = new ArrayList<>();
    public Boolean isReady = Boolean.FALSE;
    public PublicEnum.ProductType type = PublicEnum.ProductType.OTHER;
    @NotNull(message = "Preparation time is empty")
    public Integer preparationTimeMins;
    public UUID collectionId;
}
