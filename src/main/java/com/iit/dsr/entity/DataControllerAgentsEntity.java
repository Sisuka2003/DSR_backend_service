package com.iit.dsr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "data_controller_agents")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataControllerAgentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 225)
    private String username;

    @Column(name = "password", nullable = false, length = 225)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "id")
    private StatusEntity status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_role", referencedColumnName = "id")
    private UserRoleEntity userRole;

    @ManyToOne(optional = false)
    @JoinColumn(name = "controller", referencedColumnName = "id")
    private DataControllerEntity dataController;

    @Column(name = "is_logged_in", nullable = false, length = 225)
    private Integer isLoggedIn;

    @Column(name = "notifications", nullable = false)
    private Integer notifications;
}
