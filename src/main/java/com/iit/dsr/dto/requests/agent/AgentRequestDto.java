package com.iit.dsr.dto.requests.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class AgentRequestDto {
    @JsonProperty(value = "orgUsername")
    private String username;
    @JsonProperty(value = "orgPassword")
    private String password;
    @JsonProperty(value = "status")
    private Integer status;
    @JsonProperty(value = "userRole")
    private Integer userRole;
    @JsonProperty(value = "notifications")
    private Integer notifications;
    @JsonProperty(value = "agentId")
    private Integer agentId;


}
