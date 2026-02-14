package com.iit.dsr.dto.requests.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AdminRequestDto {
    @JsonProperty(value = "adminUsername")
    private String username;
    @JsonProperty(value = "adminPassword")
    private String password;
    @JsonProperty(value = "status")
    private Integer status;
    @JsonProperty(value = "adminFname")
    private String fname;
    @JsonProperty(value = "adminLname")
    private String lname;
    @JsonProperty(value = "adminId")
    private Integer id;
}
