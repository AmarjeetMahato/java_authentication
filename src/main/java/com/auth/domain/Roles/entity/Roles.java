package com.auth.domain.Roles.entity;

import com.auth.domain.Users.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Roles {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private  String id;

    @NotNull(message = "Role name can't be null")
    @Column(name = "roles", nullable = false , unique = true)
    private  String roleName;

    @Column(length = 255)
    private String description; // Role का काम समझाने के लिए

    // Many-to-Many Relationship with User
    // Roles टेबल में हम ये देख सकते हैं कि इसमें कौन-कौन से यूजर्स हैं
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

   // -------  Audit --------------
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    // Helper to generate ID if not using DB default
    @PrePersist
    public void ensureId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
