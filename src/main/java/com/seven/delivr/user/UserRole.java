package com.seven.delivr.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserRole {
    ADMIN("ADMIN"),
    VEND_ADMIN("VEND_ADMIN"),
    VENDOR("VENDOR"),
    CUSTOMER("CUSTOMER");

    UserRole(String role) {
        String[] privileges = {
                "user:r", "user:u", "user:d",
                "vendor:c", "vendor:r", "vendor:u", "vendor:d",
                "product:c", "product:r", "product:u", "product:d",
                "collection:c", "collection:r", "collection:u", "collection:d",
                "payment:c", "payment:r",
                "location:c", "location:r", "location:c", "location:d",
                "rider:r", "rider:u", "rider:d"
        };

        switch (role) {
            case "CUSTOMER" -> {
                authorities = Arrays.stream(privileges).filter(
                        p -> p.endsWith("r") || p.startsWith("user")|| p.startsWith("payment")
                ).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            }


            case "VENDOR" -> {
                authorities = Arrays.stream(privileges).filter(
                        p -> p.startsWith("user") || p.startsWith("vendor") || p.startsWith("product")
                                || p.startsWith("collection")|| p.startsWith("location")
                ).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                authorities.add(new SimpleGrantedAuthority("ROLE_VENDOR"));
            }
            case "VEND_ADMIN" -> {
                authorities = Arrays.stream(privileges).filter(
                        p -> p.startsWith("user") || p.startsWith("vendor") || p.startsWith("product")
                                || p.startsWith("collection")|| p.startsWith("location")
                ).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                authorities.add(new SimpleGrantedAuthority("ROLE_VEND_ADMIN"));
            }
            case "ADMIN" -> {
                authorities = Arrays.stream(privileges).map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }
    }

    private List<SimpleGrantedAuthority> authorities;

    public String getSpringFormat() {
        return "ROLE_" + this.name().toUpperCase();
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }
}

