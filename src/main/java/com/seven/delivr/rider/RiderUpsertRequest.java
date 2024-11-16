package com.seven.delivr.rider;

import com.seven.delivr.requests.AppRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public class RiderUpsertRequest implements AppRequest {
    public UUID id;
    @Pattern(regexp = "[a-zA-Z\\'\\- ]{2,200}", message = "Only alphabets allowed in name field between 2 to 200 characters")
    public String fullName;
    @NotBlank(message = "Phone number is a required field")
    @Pattern(regexp = "^[+-][0-9]{11,14}$", message = "incorrect phone number format")
    public String phoneNo;
}
