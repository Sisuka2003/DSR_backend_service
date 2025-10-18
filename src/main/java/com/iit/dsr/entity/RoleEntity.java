package com.iit.dsr.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    @Column(name = "last_updated_time", nullable = false)
    private Timestamp lastUpdatedTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "id")
    private StatusEntity status;
}
