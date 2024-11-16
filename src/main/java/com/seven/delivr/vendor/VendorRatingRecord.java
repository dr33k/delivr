package com.seven.delivr.vendor;

public record VendorRatingRecord(
        Long id,
        String name,
        Double rating
) {
    public static VendorRatingRecord map(Vendor v){
        return new VendorRatingRecord(
                v.getId(),
                v.getName(),
                v.getRating()
        );
    }
}
