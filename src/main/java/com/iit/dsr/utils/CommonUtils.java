package com.iit.dsr.utils;

import com.iit.dsr.dto.responses.commons.CommonResponseDTO;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CommonUtils {

    @Autowired
    private CommonResponseDTO responseDTO;

    public ResponseEntity<CommonResponseDTO> generateResponseObject(String responseStatusCode, String responseMessage, Object responseAttachments){
        return ResponseEntity.
                status(responseStatusCode.equals(Constants.RESPONSE_CODE_SUCCESS) ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(new CommonResponseDTO(responseStatusCode,responseMessage,responseAttachments));
    }
}
