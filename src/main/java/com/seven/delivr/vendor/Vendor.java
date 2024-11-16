package com.seven.delivr.vendor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seven.delivr.base.BaseLongEntity;
import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.util.Utilities;
import com.seven.delivr.vendor.account.VendorAccount;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "vendor")
@Data
public class Vendor extends BaseLongEntity{

    @Column(name = "vendor_no", nullable = false)
    private UUID vendorNo;
    @Column(name = "org_name", nullable = false)
    private String name;
    @Column(name = "mgmt_email", nullable = false, unique = true)
    private String mgmtEmail;
    @Column(name = "phone_nos", nullable = false)
    private String phoneNo;

    @Column(name= "business_type", nullable = false)
    @Enumerated
    private PublicEnum.BusinessType businessType = PublicEnum.BusinessType.OTHER;
    @Column(name="incorporation_no")
    private Integer incorporationNo = 0000001;
    @Column(name="incorporation_type", nullable = false)
    @Enumerated
    private PublicEnum.IncorporationType incorporationType = PublicEnum.IncorporationType.CAC;

    @Column
    private String address = "";
    @Column
    private String city = "";
    @Column(name="_state")
    private String state = "";
    @Column
    private String postalCode = "-1";
    @Column(nullable = false)
    private String country="";
    @Column
    private String logo = "";
    @Column(name = "weekday_hours")
    private String weekdayHours = "";
    @Column(name = "weekend_hours")
    private String weekendHours = "";
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendor")
    private List<VendorAccount> vendorAccounts = new ArrayList<>();
    @Column
    private Double rating = 0.0;

    public String getLocationString(){
        return String.format("%s, %s, %s, %s", address, city, state, country);
    }
    public String getNameAndLocation(){
        return String.format("\n%s\n%s\n", name, getLocationString());
    }

    //Constructor is used by Vendor::creationMap
    public Vendor(){
        this.vendorNo = UUID.randomUUID();
    }
//Constructor is used by VendorRepository::getVendorWithRatings
    public Vendor(Long id, String name, Double rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }
    //Constructor is used by VendorService::filter
    public Vendor(Long id, String name, String mgmtEmail, String logo, ZonedDateTime dateCreated) {
        this.id = id;
        this.name = name;
        this.mgmtEmail = mgmtEmail;
        this.logo = logo;
        this.dateCreated = dateCreated;
    }

    public static Vendor creationMap(VendorCreateRequest request){
        Vendor vendor = new Vendor();
        vendor.setName(Utilities.escape(request.name));
        vendor.setIncorporationNo(Integer.parseInt(request.incorporationNo));
        vendor.setBusinessType(request.businessType);
        vendor.setMgmtEmail(request.mgmtEmail);
        vendor.setPhoneNo(request.phoneNo);
        vendor.setIncorporationType(PublicEnum.IncorporationType.CAC);
        vendor.setAddress(Utilities.escape(request.address));
        vendor.setCity(request.city.toUpperCase());
        vendor.setState(request.state.toUpperCase());
        vendor.setCountry(request.country.toUpperCase());
        vendor.setPostalCode(request.postalCode);
        vendor.setWeekdayHours(request.weekdayHours);
        vendor.setWeekendHours(request.weekendHours);
        return vendor;
    }

    public static void updateMap(VendorPatchRequest request, Vendor vendor){
        if (!Utilities.isEmpty(request.address)) vendor.setAddress(Utilities.escape(Utilities.clean(request.address,"[^A-Za-z0-9\\-\\'\\(\\) ]")));
        if (!Utilities.isEmpty(request.name)) vendor.setName(Utilities.escape(Utilities.clean(request.name,"[^A-Za-z0-9\\-\\'\\(\\) ]")));
        if (!Utilities.isEmpty(request.city)) vendor.setCity(Utilities.clean(request.city.toUpperCase(),"[^A-Z ]"));
        if (!Utilities.isEmpty(request.state)) vendor.setState(Utilities.clean(request.state.toUpperCase(),"[^A-Z ]"));
        if (!Utilities.isEmpty(request.postalCode)) vendor.setPostalCode(Utilities.clean(request.postalCode, "[^0-9]{4,}"));
        if (!Utilities.isEmpty(request.country)) vendor.setCountry(Utilities.clean(request.country.toUpperCase(),"[^A-Z ]"));
        if (!Utilities.isEmpty(request.logo)) vendor.setLogo(request.logo);
        if (!Utilities.isEmpty(request.weekdayHours)) vendor.setWeekdayHours(Utilities.clean(request.weekdayHours,"[^0-9\\-\\: ]"));
        if (!Utilities.isEmpty(request.weekendHours)) vendor.setWeekendHours(Utilities.clean(request.weekendHours,"[^0-9\\-\\: ]"));
        if (!Utilities.isEmpty(request.mgmtEmail)) vendor.setMgmtEmail(Utilities.clean(request.mgmtEmail,"[^a-zA-Z0-9@.+$_\\-]"));
        if (!Utilities.isEmpty(request.phoneNo)) vendor.setPhoneNo(Utilities.clean(request.phoneNo, "[^0-9+\\-]"));
        if (request.businessType != null) vendor.setBusinessType(request.businessType);
    }

    public List<VendorAccount> getVendorAccounts(){
        return this.vendorAccounts;
    }

    public boolean equals(BaseLongEntity other){
        return super.equals(other);
    }

    public int hashCode(){
        return super.hashCode();
    }
}
