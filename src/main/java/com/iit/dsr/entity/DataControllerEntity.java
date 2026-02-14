package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_controller_org")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataControllerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "org_name", nullable = false, length = 225)
    private String orgName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "org_status", referencedColumnName = "id")
    private StatusEntity orgStatus;

    @Column(name = "created_time", nullable = false)
    private String createdTime;

    @Column(name = "last_updated_time", nullable = false)
    private String lastUpdatedTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "key_identification", referencedColumnName = "id")
    private IdentificationKeyTypesEntity identificationKey;

    @Column(name = "org_login_username", nullable = false, length = 45)
    private String orgUsername;

    @Column(name = "org_login_password", nullable = false, length = 45)
    private String orgPassword;

    @Column(name = "notifications", nullable = false)
    private int notifications;

    @Column(name = "agents", nullable = false)
    private int agents;
}
