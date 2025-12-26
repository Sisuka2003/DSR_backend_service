package com.iit.dsr.dto.requests.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataControllerLoginRequestDTO {

    @JsonProperty("orgUsername")
    private String orgUsername;

    @JsonProperty("orgPassword")
    private String orgPassword;

}
