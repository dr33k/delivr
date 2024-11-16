package com.seven.delivr.vendor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.requests.AppRequest;
import com.seven.delivr.enums.PublicEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class VendorCreateRequest implements AppRequest{

    @NotBlank(message = "Business name not be blank")
    @Pattern(regexp = "^[A-Za-z0-9\\-\\'\\(\\) ]{2,100}$", message = "Only alphabets and numbers allowed")
    public String name;

    @NotBlank(message = "Email must not be blank")
    @Email(regexp = "^.+@.+\\..+$", message = "Incorrect email format")
    public String mgmtEmail;
    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^[+-][0-9]{11,14}$", message = "Incorrect phone number format")
    public String phoneNo;

//    @NotBlank(message = "Incorporation Number must not be blank")
//    @Pattern(regexp = "^[0-9]{7}$", message = "Incorrect incorporation number format. 7 digit number only")
    @JsonIgnore
    public String incorporationNo = "0000001";
    @JsonIgnore
    public PublicEnum.IncorporationType incorporationType = PublicEnum.IncorporationType.CAC;
    public PublicEnum.BusinessType businessType = PublicEnum.BusinessType.OTHER;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9\\-\\'\\(\\)\\.\\, ]{2,200}$", message ="Address: only  2 - 200 characters (Alphabets, numbers, periods and commas) allowed")
    public String address;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z ]{2,30}$", message = "City: only alphabets between 2 - 30 characters")
    public String city;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z ]{2,30}$", message = "State: only alphabets between 2 - 30 characters")
    public String state;
    @NotBlank
    @Pattern(regexp="^[0-9]{5,10}$", message="Postal code has at least 5 digits")
    public String postalCode;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z ]{2,50}$",message = "Country: only alphabets between 2 - 50 characters")
    public String country;
    @NotBlank
    @Pattern(regexp="^[0-9][0-9]:[0-9][0-9]\\-[0-9][0-9]:[0-9][0-9]$", message="Weekday Hours format: 00:00-00:00")
    public String weekdayHours;
    @NotBlank
    @Pattern(regexp="^[0-9][0-9]:[0-9][0-9]\\-[0-9][0-9]:[0-9][0-9]$", message="Weekday Hours format: 00:00-00:00")
    public String weekendHours;

}
