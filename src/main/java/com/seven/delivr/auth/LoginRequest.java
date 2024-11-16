package com.seven.delivr.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public class LoginRequest {
    @NotBlank
    @Email(regexp = "^.+@.+\\..+$", message = "Incorrect email format")
    public String username; //usually email
    @NotBlank
    public String password;
}
