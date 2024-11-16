package com.seven.delivr.vendor.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class VendorAccountCreateRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Alias: Only letters and numbers allowed")
    public String alias;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z ]*$",  message = "Account Name: Only letters allowed")
    public String accountName;
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$",  message = "Account Number: Only a 10-digit number allowed")
    public String accountNo;
    @NotBlank
    @Pattern(regexp = "^[0-9]{3,6}$", message = "Bank Code: Only 3 - 6 digits allowed")
    public String bankCode;
    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$",  message = "Country: Only 2 letters allowed")
    public String country;
}
