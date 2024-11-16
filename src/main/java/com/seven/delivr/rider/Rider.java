package com.seven.delivr.rider;

import com.seven.delivr.base.BaseUUIDEntity;
import com.seven.delivr.util.Utilities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rider")
@Data
@NoArgsConstructor
public class Rider extends BaseUUIDEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, name = "phone_no")
    private String phoneNo;

    public Rider(String name, String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public static Rider creationMap(RiderUpsertRequest request){
        return new Rider(
                Utilities.escape(request.fullName),
                request.phoneNo);
    }
    public static void updateMap(RiderUpsertRequest request, Rider rider){
        rider.setName(Utilities.escape(request.fullName));
        rider.setPhoneNo(request.phoneNo);
    }
}
