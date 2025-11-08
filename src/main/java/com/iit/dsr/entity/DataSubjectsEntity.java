package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "data_subjects")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataSubjectsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 225)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email_address", nullable = false, unique = true, length = 45)
    private String emailAddress;

    @Column(name = "nic_number", nullable = false, unique = true, length = 45)
    private String nicNumber;

    @Column(name = "contact_number", nullable = false, unique = true, length = 45)
    private String mobileNumber;

    @Column(name = "customer_id", nullable = false, unique = true, length = 45)
    private String customerId;

    @Column(nullable = false, length = 225)
    private String password;

    @Column(name = "is_verified", nullable = false)
    private Integer isVerified;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "id")
    private StatusEntity status;

    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    @Column(name = "last_updated_time", nullable = false)
    private Timestamp lastUpdatedTime;
}
