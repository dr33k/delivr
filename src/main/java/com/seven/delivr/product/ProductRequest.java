package com.seven.delivr.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.requests.AppRequest;

public class ProductRequest implements AppRequest {
    @JsonIgnore
    public Operation operation = null;
    public enum Operation{CREATE, UPDATE}
}
