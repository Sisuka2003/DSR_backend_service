package com.iit.dsr.dto.requests.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CommonRequestDto {
    @JsonProperty("isAdminDashboard")
    private Boolean isAdminDashboard;

}
