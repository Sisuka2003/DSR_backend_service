package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
    private Timestamp createdTime;

    @Column(name = "last_updated_time", nullable = false)
    private Timestamp lastUpdatedTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "key_identification", referencedColumnName = "id")
    private IdentificationKeyTypesEntity identificationKey;
}
