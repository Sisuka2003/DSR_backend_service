package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "data_subject_in_org")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataSubjectInOrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "id")
    private StatusEntity status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ds_code", referencedColumnName = "id")
    private DataSubjectsEntity dsCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dc_code", referencedColumnName = "id")
    private DataControllerEntity dcCode;

    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    @Column(name = "last_updated_time", nullable = false)
    private Timestamp lastUpdatedTime;

    @Column(name = "collected_data", nullable = false)
    private String collectedData;

    @ManyToOne(optional = false)
    @JoinColumn(name = "activity_status", referencedColumnName = "id")
    private StatusEntity activityStatus;

    @Column(name = "backuped_data", nullable = false)
    private String backupData;
}
