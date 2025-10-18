package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dsr_request")
public class DsrRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "request_description", nullable = false, length = 225)
    private String requestDescription;

    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    @Column(name = "last_updated_time", nullable = false)
    private Timestamp lastUpdatedTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "request", referencedColumnName = "id")
    private RequestEntity request;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private UserEntity user;
}
