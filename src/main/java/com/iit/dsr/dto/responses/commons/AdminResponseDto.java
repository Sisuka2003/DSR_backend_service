package com.iit.dsr.dto.responses.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AdminResponseDto {
    private Integer id;
    private String username;
    private String password;
    private String status;
    private String userRole;
    private String firstName;
    private String lastName;
}
