package com.seven.delivr.user.location;

import java.util.UUID;

public record LocationRecord(
        UUID id,
        String address,
        String city,
        String state,
        String postalCode,
        String country) {
    public static LocationRecord map(Location l) {
        return new LocationRecord(
                l.getId(),
                l.getAddress(),
                l.getCity(),
                l.getState(),
                l.getPostalCode(),
                l.getCountry()
        );
    }
}
