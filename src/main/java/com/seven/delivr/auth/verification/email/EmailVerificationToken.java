package com.seven.delivr.auth.verification.email;

import com.seven.delivr.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "email_verification_token")
public class EmailVerificationToken {
    @Id
    @Column(name = "user_no")
    private UUID userNo;
    @Column
    private String token;
    @CreationTimestamp
    @Column(name = "_timestamp")
    private ZonedDateTime timestamp;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
