package com.seven.delivr.auth.verification.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class OTPConfirmationRequest {
    @Pattern(regexp = "^[0-9]{6}$", message = "Must be a 6 digit number")
    public String pin;

    @NotBlank(message = "Must not be blank")
    public String username;
}
