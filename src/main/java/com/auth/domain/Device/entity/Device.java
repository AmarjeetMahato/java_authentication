package com.auth.domain.Device.entity;

import com.auth.domain.Users.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "devices")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Device {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id;

    // ---------------- DEVICE INFO ----------------
    @Column(name = "device_name", nullable = false)
    private String deviceName; // e.g. Chrome on Windows

    @Column(name = "device_type", nullable = false)
    private String deviceType; // MOBILE / DESKTOP / TABLET

    @Column(name = "os", nullable = false)
    private String os; // Windows, Android, iOS

    @Column(name = "browser")
    private String browser;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    // ---------------- AUTH / SECURITY ----------------
    @Column(name = "refresh_token", length = 2000)
    private String refreshToken;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_trusted", nullable = false)
    private Boolean isTrusted;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    // ---------------- SESSION ----------------
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    // ---------------- AUDIT ----------------
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    // ---------------- USER RELATION ----------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // ---------------- PRE-PERSIST ----------------
    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }

        if (isActive == null) {
            isActive = true;
        }

        if (isTrusted == null) {
            isTrusted = false;
        }

        if (isBlocked == null) {
            isBlocked = false;
        }

        if (lastLoginAt == null) {
            lastLoginAt = LocalDateTime.now();
        }
    }
}