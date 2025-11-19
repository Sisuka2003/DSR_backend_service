package com.iit.dsr.dto.requests.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataSubjectLoginRequestDTO {

    @JsonProperty("emailAddress")
    private String emailAddress;
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("nicNumber")
    private String nicNumber;
    @JsonProperty("customerID")
    private String customerID;
    @JsonProperty("organizationID")
    private String organizationID;
}
