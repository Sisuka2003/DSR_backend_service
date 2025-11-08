package com.iit.dsr.dto.responses.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CommonResponseDTO {
    private String responseStatusCode;
    private String responseMessage;
    private Object data;
}
