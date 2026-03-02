package com.iit.dsr.dto.requests.operations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataControllerOperationsRequestDto {
    @JsonProperty("dcCode")
    private String dcCode;
    @JsonProperty("dsCode")
    private String dsCode;
    @JsonProperty("mapId")
    private int mapId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("isUserProfile")
    private Boolean isUserProfile;

    @JsonProperty("orgName")
    private String orgName;
    @JsonProperty("idKey")
    private String idKey;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;
    @JsonProperty("agentCount")
    private String agentCount;


    @JsonProperty("agentCode")
    private String agentCode;
    @JsonProperty("isAgentAlert")
    private Boolean isAgentAlert;

    @JsonProperty("isTaskAssign")
    private Boolean isTaskAssign;

}
