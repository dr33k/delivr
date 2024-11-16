package com.seven.delivr.product;

import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public class ProductFlagRequest extends ProductRequest{
    private UUID productNo;
    private Boolean isPublished;
    private Boolean isReady;

    public ProductFlagRequest(){}
    public UUID getProductNo() {
        return productNo;
    }

    public void setProductNo(UUID productNo) {
        this.productNo = productNo;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean published) {
        isPublished = published;
    }

    public Boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(Boolean ready) {
        isReady = ready;
    }
}
