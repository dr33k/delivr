package com.seven.delivr.auth.verification.email;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name="otp")
@Data
public class OTP {
    @Id
    private Integer pin;
    @Column
    private String username;
    @CreationTimestamp
    @Column(name = "_timestamp")
    private ZonedDateTime timestamp;
    public OTP(){}

    public OTP(Integer pin, String username) {
        this.pin = pin;
        this.username = username;
    }
}
