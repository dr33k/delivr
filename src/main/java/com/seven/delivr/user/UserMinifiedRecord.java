package com.seven.delivr.user;

import java.time.ZonedDateTime;
import java.util.UUID;

public record UserMinifiedRecord (
        UUID id,
        String fname,
        String lname,
        String email,
        ZonedDateTime dateCreated
){
    public static UserMinifiedRecord map(User u) {
        return new UserMinifiedRecord(
                u.getId(),
                u.getFname(),
                u.getLname(),
                u.getEmail(),
                u.getDateCreated()
        );
    }

    }
