package com.gpse.sesam.domain.user;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PasswordResetToken {
    private static final long EXPIRATION = 60 * 24 * 60000L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = SesamUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private SesamUser user;

    @Column(nullable = false)
    private Date expiryDate;

    protected PasswordResetToken() {
    }

    public PasswordResetToken(SesamUser user, String token) {
        this.user = user;
        this.token = token;
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION);
    }

    public SesamUser getUser() {
        return user;
    }

    public boolean isExpired() {
        return new Date().after(expiryDate);
    }
}
