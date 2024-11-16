package com.seven.delivr.vendor;

import com.seven.delivr.enums.PublicEnum;

import java.time.ZonedDateTime;
import java.util.UUID;

public record VendorRecord(
    Long id,
    UUID vendorNo,
    String name,
    String mgmtEmail,
    String phoneNo,
    PublicEnum.BusinessType businessType,
    String address,
    String city,
    String state,
    String postalCode,
    String country,
    String logo,
    String weekdayHours,
    String weekendHours,
//    Set<String> productTags,
    ZonedDateTime dateCreated){
    public static VendorRecord map(Vendor v){
        return new VendorRecord(
                v.getId(),
        v.getVendorNo(),
        v.getName(),
        v.getMgmtEmail(),
        v.getPhoneNo(),
        v.getBusinessType(),
        v.getAddress(),
        v.getCity(),
        v.getState(),
        v.getPostalCode(),
        v.getCountry(),
        v.getLogo(),
        v.getWeekdayHours(),
        v.getWeekendHours(),
//        v.getProductTags().stream().map(ProductTag::getName).collect(Collectors.toSet()),
        v.getDateCreated());

    }

}
