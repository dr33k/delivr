package com.seven.delivr.user.location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seven.delivr.base.BaseUUIDEntity;
import com.seven.delivr.user.User;
import com.seven.delivr.user.UserUpsertRequest;
import com.seven.delivr.util.Utilities;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user_location")
@Data
public class Location extends BaseUUIDEntity{
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;
    private String address;
    @Column(nullable = false)
    private String city;
    @Column(name="_state", nullable = false)
    private String state;
    @Column
    private String postalCode = "-1";
    @Column(nullable = false)
    private String country;

    public Location(){}
    public Location(String address, String city, String state, String postalCode, String country) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public static Location creationMap(UserUpsertRequest r){
        return new Location(
                Utilities.escape(r.address),
                r.city.toUpperCase(),
                r.state.toUpperCase(),
                r.postalCode,
                r.country.toUpperCase());
    }
    public static Location creationMap(LocationUpsertRequest r){
        return new Location(
                Utilities.escape(r.address),
                r.city.toUpperCase(),
                r.state.toUpperCase(),
                r.postalCode,
                r.country.toUpperCase());
    }

    public static void updateMap(LocationUpsertRequest r, Location l){
        l.setAddress(Utilities.escape(r.address));
        l.setCity(r.city.toUpperCase());
        l.setState(r.state.toUpperCase());
        l.setPostalCode(r.postalCode);
        l.setCountry(r.country.toUpperCase());
    }

    public String toString(){return String.format("%s, %s, %s, %s", address, city, state, country);}
    public boolean equals(BaseUUIDEntity other){
        return super.equals(other);
    }

    public int hashCode(){
        return super.hashCode();
    }
}
