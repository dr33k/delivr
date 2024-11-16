package com.seven.delivr.vendor;

import java.time.ZonedDateTime;

public record VendorMinifiedRecord (
        Long id,
        String name,
        String mgmtEmail,
        String logo,
        ZonedDateTime dateCreated
) {
    public static VendorMinifiedRecord map(Vendor v) {
        return new VendorMinifiedRecord(
                v.getId(),
                v.getName(),
                v.getMgmtEmail(),
                v.getLogo(),
                v.getDateCreated()
        );
    }
}
