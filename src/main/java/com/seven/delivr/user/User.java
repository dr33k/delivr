package com.seven.delivr.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seven.delivr.rider.Rider;
import com.seven.delivr.user.location.Location;
import com.seven.delivr.util.Utilities;
import com.seven.delivr.vendor.Vendor;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_no", unique = true)
    private UUID userNo = UUID.randomUUID();

    @Column(nullable = false)
    private String fname;
    @Column(nullable = false)
    private String lname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "phone_no", nullable = false)
    private String phoneNo;
    @Column(name="_password", nullable = false)
    private String password;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Location> locations = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "_role", nullable = false)
    private UserRole role = UserRole.CUSTOMER ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rider_id", referencedColumnName = "id")
    private Rider rider;

    @Column
    private Boolean enabled = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "date_created")
    private ZonedDateTime dateCreated;
    @UpdateTimestamp
    @Column(name = "date_modified")
    private ZonedDateTime dateModified;

    public User(){}

    public User(String fname, String lname, String email, String phoneNo, UserRole role) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.role = role;
    }


    public static User creationMap(UserUpsertRequest r) {
    return new User(
            Utilities.escape(r.fname),
            Utilities.escape(r.lname),
            r.email,
            r.phoneNo,
            r.role == null ? UserRole.CUSTOMER: r.role
    );
    }
    public static void updateMap(UserUpsertRequest r, User u) {
            u.setFname(Utilities.escape(r.fname));
            u.setLname(Utilities.escape(r.lname));
            u.setPhoneNo(r.phoneNo);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    public String getFullName(){return String.format("%s %s", fname, lname);}
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean equals(User another){
        return this.id.equals(another.getId());
    }
    public int hashCode(){
        return super.hashCode();
    }
}