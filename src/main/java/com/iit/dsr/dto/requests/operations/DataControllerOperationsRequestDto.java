package com.iit.dsr.dto.requests.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

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

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
