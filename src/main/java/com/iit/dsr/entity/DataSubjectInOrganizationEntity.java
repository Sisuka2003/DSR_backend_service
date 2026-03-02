package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @CreationTimestamp
    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    @UpdateTimestamp
    @Column(name = "last_updated_time", nullable = false)
    private Timestamp lastUpdatedTime;

    @Column(name = "collected_data", nullable = false)
    private String collectedData;

    @ManyToOne(optional = false)
    @JoinColumn(name = "activity_status", referencedColumnName = "id")
    private StatusEntity activityStatus;

    @Column(name = "backuped_data", nullable = false)
    private String backupData;

    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_activity_status", referencedColumnName = "id")
    private StatusEntity adminActivityStatus;


    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_activity_status", referencedColumnName = "id")
    private StatusEntity subjectActivityStatus;

    @ManyToOne(optional = true)
    @JoinColumn(name = "agent_code", referencedColumnName = "id")
    private DataControllerAgentsEntity agentCode;
}
