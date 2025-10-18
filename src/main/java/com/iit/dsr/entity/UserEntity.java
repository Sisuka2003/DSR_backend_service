package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 225)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 225)
    private String email;

    @Column(nullable = false, length = 225)
    private String password;

    @Column(name = "is_verified", nullable = false)
    private Integer isVerified;  // You can later change this to Boolean if desired

    // --- Relationships ---
    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "id")
    private StatusEntity status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role", referencedColumnName = "id")
    private RoleEntity role;

    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    @Column(name = "last_updated_time", nullable = false)
    private Timestamp lastUpdatedTime;
}
