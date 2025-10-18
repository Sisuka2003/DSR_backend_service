package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Entity
@Table(name = "status")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "created_time")
    private Timestamp createdDateTime;

    @Column(name = "last_updated_time")
    private Timestamp lastUpdatedDateTime;
}
