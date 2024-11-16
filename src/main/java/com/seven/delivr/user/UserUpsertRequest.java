package com.seven.delivr.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.requests.AppRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public class UserUpsertRequest implements AppRequest {
    @Nullable
    public UUID id;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z\\'\\- ]{2,35}", message = "Only alphabets allowed in name field between 2 to 35 characters")
    public String fname;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z\\-\\' ]{2,35}", message = "Only alphabets allowed in name field between 2 to 35 characters")
    public String lname;
    @Email(regexp = "^[A-Za-z0-9\\.\\+\\-\\$\\_]+@[A-Za-z0-9\\.\\+\\-\\$\\_]+\\.[A-Za-z0-9\\.\\+\\-\\$\\_]+$", message = "Incorrect email format")
    public String email;
    @NotBlank
    public String password="password";
    @NotBlank(message = "Phone number is a required field")
    @Pattern(regexp = "^[+-][0-9]{11,14}$", message = "incorrect phone number format")
    public String phoneNo;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9\\-\\'\\(\\)\\.\\, ]{2,200}$", message = "Address: Only alphabets and numbers allowed along with (,.' and -). No newlines")
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

    public UserRole role;

    @Nullable
    @Pattern(regexp = "^[0-9]{6}$")
    public String otp;
    @JsonIgnore
    public Operation operation = null;


    public enum Operation{CREATE, UPDATE}
}
