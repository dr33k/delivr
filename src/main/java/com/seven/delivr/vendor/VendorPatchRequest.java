package com.seven.delivr.vendor;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
@Validated
public class VendorPatchRequest extends VendorCreateRequest{
    @NotNull
    public Long id;
    public String logo;
}
