package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 225)
    private String username;

    @Column(name = "password", nullable = false, length = 225)
    private String password;

    @Column(name = "firstname", nullable = false, length = 225)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 225)
    private String lastName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "id")
    private StatusEntity status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role", referencedColumnName = "id")
    private UserRoleEntity userRole;
}
