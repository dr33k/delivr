package com.seven.delivr.user.location;

import com.seven.delivr.requests.AppRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public class LocationUpsertRequest implements AppRequest {
    @Nullable
    public UUID id;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9\\n\\-\\'\\(\\)\\.\\, ]{2,200}$", message = "Address must contain only numbers and alphabets between 2 to 200 characters")
    public String address;
    @NotBlank
    @Pattern(regexp = "[A-Za-z ]{2,30}", message = "City: only alphabets between 2 - 30 characters")
    public String city;
    @NotBlank
    @Pattern(regexp = "[A-Za-z ]{2,30}", message = "State: only alphabets between 2 - 30 characters")
    public String state;
    @NotBlank
    @Pattern(regexp="[0-9]{3,10}", message="Postal code has at least 3 digits")
    public String postalCode;
    @NotBlank
    @Pattern(regexp = "[A-Za-z ]{2,50}",message = "Country: only alphabets between 2 - 50 characters")
    public String country;
}
