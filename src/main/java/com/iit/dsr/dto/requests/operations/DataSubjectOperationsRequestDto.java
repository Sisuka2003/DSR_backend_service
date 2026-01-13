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
public class DataSubjectOperationsRequestDto {
    @JsonProperty("dcCode")
    private String dcCode;
    @JsonProperty("dsCode")
    private String dsCode;
    @JsonProperty("collectedData")
    private String collectedData;
    @JsonProperty("mapId")
    private int mapId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("IsControllerApproved")
    private boolean IsControllerApproved;
    @JsonProperty("recipientEmail")
    private String recipientEmail;
    @JsonProperty("otpCode")
    private String otpCode;
}
