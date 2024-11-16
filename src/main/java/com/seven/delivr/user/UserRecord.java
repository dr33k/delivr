package com.seven.delivr.user;

import com.seven.delivr.user.location.LocationRecord;
import com.seven.delivr.vendor.VendorRecord;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record UserRecord(
        UUID id,
        UUID userNo,
        String fname,
        String lname,
        String email,
        String phoneNo,
        UserRole role,
        List<LocationRecord> locations,
        VendorRecord vendor,
        ZonedDateTime dateCreated,
        ZonedDateTime dateModified

) {

    public static UserRecord map(User u){
        return new UserRecord(
                u.getId(),
                u.getUserNo(),
                u.getFname(),
                u.getLname(),
                u.getEmail(),
                u.getPhoneNo(),
                u.getRole(),
                u.getLocations().stream().map(LocationRecord::map).toList(),
                u.getVendor() != null? VendorRecord.map(u.getVendor()): null,
                u.getDateCreated(),
                u.getDateModified()
        );
    }
}
